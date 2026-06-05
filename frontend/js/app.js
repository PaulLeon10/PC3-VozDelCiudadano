const API_BASE = "http://localhost:8080/api";
let propuestasLocales = [];

function cambiarPestaña(pestaña) {
    document.querySelectorAll('[id^="vista-"]').forEach(el => el.classList.add('d-none'));
    document.querySelectorAll('.nav-link').forEach(el => el.classList.remove('active'));
    
    document.getElementById(`vista-${pestaña}`).classList.remove('d-none');
    document.getElementById(`btn-${pestaña}`).classList.add('active');

    if (pestaña === 'ciudadano' || pestaña === 'sistema') {
        cargarPropuestas();
    }
}
// CU-01: Registrar Propuesta Normativa
document.getElementById('form-propuesta').addEventListener('submit', async (e) => {
    e.preventDefault();
    const titulo = document.getElementById('titulo').value;
    const textoBase = document.getElementById('textoBase').value;

    try {
        const response = await fetch(`${API_BASE}/propuestas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ titulo, textoBase })
        });

        if (response.ok) {
            mostrarAlerta("Propuesta registrada correctamente", "bg-success");
            document.getElementById('form-propuesta').reset();
        } else {
            mostrarAlerta("Error en los parámetros de la solicitud.", "bg-danger");
        }
    } catch (err) {
        mostrarAlerta("Error en la comunicación: El servidor backend no responde.", "bg-danger");
    }
});

// Cargar catálogo de propuestas desde el backend
async function cargarPropuestas() {
    try {
        const response = await fetch(`${API_BASE}/propuestas`);
        
        // Validación de seguridad para asegurar que la respuesta sea JSON válido
        if (!response.ok) throw new Error("Error en la respuesta del servidor");
        propuestasLocales = await response.json();
        
        const select = document.getElementById('select-propuestas');
        select.innerHTML = '<option value="">Seleccione una propuesta de la lista</option>';
        
        const tableBody = document.getElementById('tabla-sistema');
        tableBody.innerHTML = '';

        propuestasLocales.forEach(p => {
            if(p.estado === "ACTIVA") {
                select.innerHTML += `<option value="${p.id}">${p.titulo}</option>`;
            }

            let badgeClass = p.estado === 'ACTIVA' ? 'bg-success' : p.estado === 'CONGELADA' ? 'bg-danger' : 'bg-primary';
            let hashDisplay = p.hashSeguridad ? `<code>${p.hashSeguridad.substring(0, 24)}...</code>` : '<span class="text-muted small">Abierto a firmas</span>';
            let actionButton = p.estado === 'ACTIVA' 
                ? `<button class="btn btn-sm btn-outline-dark fw-semibold" onclick="forzarCierre(${p.id})">Cierre Técnico</button>` 
                : `<button class="btn btn-sm btn-light border" disabled>Procesado</button>`;

            tableBody.innerHTML += `
                <tr>
                    <td>${p.id}</td>
                    <td><strong>${p.titulo}</strong></td>
                    <td class="text-center font-monospace">${p.contadorFirmas}</td>
                    <td><span class="badge ${badgeClass}">${p.estado}</span></td>
                    <td class="font-monospace">${hashDisplay}</td>
                    <td class="text-center">${actionButton}</td>
                </tr>
            `;
        });
    } catch (err) {
        console.error("Error al sincronizar con la API:", err);
    }
}

function actualizarDetallePropuesta() {
    const selectValue = document.getElementById('select-propuestas').value;
    const zona = document.getElementById('zona-firma');
    
    if (!selectValue) { zona.classList.add('d-none'); return; }
    
    const id = parseInt(selectValue, 10);
    const p = propuestasLocales.find(prop => prop.id === id);
    
    if (p) {
        zona.classList.remove('d-none');
        document.getElementById('detalle-titulo').innerText = p.titulo;
        document.getElementById('detalle-firmas').innerText = p.contadorFirmas;
        
        const badge = document.getElementById('detalle-estado');
        badge.innerText = p.estado;
        badge.className = "badge bg-success";
    }
}

// CU-02: Enviar Firma (lo intercepta el Proxy de control de duplicados)
document.getElementById('form-firma').addEventListener('submit', async (e) => {
    e.preventDefault();
    const propuestaId = document.getElementById('select-propuestas').value;
    const ciudadanoId = document.getElementById('ciudadanoId').value;
    const comentario = document.getElementById('comentario').value;

    try {
        const response = await fetch(`${API_BASE}/firmas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ ciudadanoId, propuestaId, comentario })
        });

        // Se verifica de forma segura si la respuesta contiene un JSON antes de parsearlo
        let data = {};
        if (response.headers.get("content-type")?.includes("application/json")) {
            data = await response.json();
        }

        if (response.status === 200 || response.status === 201) {
            mostrarAlerta("Firma procesada. Se optimizó el espacio en memoria y se anexó la enmienda.", "bg-success");
            document.getElementById('form-firma').reset();
            cargarPropuestas();
            document.getElementById('zona-firma').classList.add('d-none');
            document.getElementById('select-propuestas').value = "";
        } else if (response.status === 400) {
            mostrarAlerta(`Acceso Denegado (Proxy): ${data.error || 'Petición inválida'}`, "bg-danger");
        } else {
            mostrarAlerta("Error inesperado en el servidor de firmas.", "bg-danger");
        }
    } catch (err) {
        mostrarAlerta("Error en la ejecución del servicio de firmas.", "bg-danger");
    }
});

// CU-04: Cierre y sello criptográfico vía Facade
async function forzarCierre(id) {
    try {
        const response = await fetch(`${API_BASE}/propuestas/${id}/simular-cierre`, {
            method: 'POST'
        });
        
        if(response.ok) {
            mostrarAlerta("La estructura ha sido consolidada. El expediente fue bloqueado y sellado mediante Hash SHA-256.", "bg-dark");
            cargarPropuestas();
        } else {
            mostrarAlerta("No se pudo completar el cierre técnico de la iniciativa.", "bg-danger");
        }
    } catch(err) {
        mostrarAlerta("Error de red al intentar acceder al servicio de despacho.", "bg-danger");
    }
}

function mostrarAlerta(mensaje, claseBg) {
    const toastEl = document.getElementById('liveToast');
    toastEl.className = `toast align-items-center text-white border-0 ${claseBg}`;
    document.getElementById('toast-mensaje').innerText = mensaje;
    
    toastEl.classList.add('show');
    setTimeout(() => { toastEl.classList.remove('show'); }, 4000);
}