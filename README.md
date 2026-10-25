# Kafka Order Platform 🚀

Plataforma basada en una arquitectura orientada a eventos (**Event-Driven Architecture**) construida con **Spring Boot**, **Apache Kafka** y **Docker**. Permite la creación y publicación de pedidos de forma asíncrona, desacoplando el procesamiento de compras de los servicios de notificación.

🔗 **Repositorio:** [https://github.com/Jhonatanisai8/kafka-order-platform](https://github.com/Jhonatanisai8/kafka-order-platform)

---

## 🏛️ Arquitectura del Sistema

```
  [ Cliente / Postman ]
           │  HTTP POST /api/orders
           ▼
┌───────────────────────┐
│     order-service     │  (Producer - Puerto 8080)
│   (Spring Boot 4.x)   │
└──────────┬────────────┘
           │ Publica evento en tópico 'order-events' (3 particiones)
           ▼
┌───────────────────────┐         ┌───────────────────────┐
│      Apache Kafka     │ ◄───────┤       Kafka UI        │ (Dashboard: :8085)
│     (Broker :9092)    │         └───────────────────────┘
└──────────┬────────────┘
           │ Consume eventos ('notification-group')
           ▼
┌───────────────────────┐
│ notification-service  │  (Consumer - Background Worker)
│   (Spring Boot 4.x)   │  Simula envío de correos / comprobantes
└───────────────────────┘
```

---

## 📦 Componentes y Tecnologías

- **Java 21** & **Spring Boot 4 / Spring Kafka**
- **Apache Kafka** (modo KRaft) ejecutado sobre Docker
- **Kafka UI** para visualización e inspección de tópicos, mensajes, particiones y grupos de consumidores
- **Lombok** & **Jackson** para serialización / deserialización JSON

### Servicios

| Servicio | Rol | Puerto / Detalle |
| :--- | :--- | :--- |
| **Kafka Broker** | Mensajería / Broker | `localhost:9092` (interno `29092`) |
| **Kafka UI** | Dashboard web de administración | `http://localhost:8085` |
| **order-service** | Microservicio Productor (REST API) | `http://localhost:8080` |
| **notification-service** | Microservicio Consumidor (Worker) | Grupo: `notification-group` |

---

## 🛠️ Requisitos Previos

- **Java 21** o superior
- **Docker** y **Docker Compose**
- **Git**

---

## 🚀 Guía de Puesta en Marcha

### 1. Clonar el repositorio

```bash
git clone https://github.com/Jhonatanisai8/kafka-order-platform.git
cd kafka-order-platform
```

### 2. Levantar la infraestructura (Kafka y Kafka UI)

Inicia los contenedores de Kafka y Kafka UI con Docker Compose:

```bash
docker compose up -d
```

> Accede a **Kafka UI** en [http://localhost:8085](http://localhost:8085) para monitorear el clúster.

### 3. Iniciar `order-service`

En una terminal nueva:

```bash
cd order-service
./mvnw clean spring-boot:run
```

*(En Windows CMD / PowerShell: `.\mvnw.cmd clean spring-boot:run`)*

### 4. Iniciar `notification-service`

En otra terminal nueva:

```bash
cd notification-service
./mvnw clean spring-boot:run
```

*(En Windows CMD / PowerShell: `.\mvnw.cmd clean spring-boot:run`)*

---

## 🧪 Pruebas de Funcionamiento

### Crear un nuevo pedido

Envía una solicitud HTTP POST al `order-service`:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerEmail": "juan.perez@example.com",
    "product": "Laptop Lenovo ThinkPad",
    "price": 1250.00
  }'
```

### Flujo de respuesta:
1. `order-service` asigna un UUID (`orderId`), fija el estado en `PENDING` y publica el mensaje en el tópico `order-events`.
2. Responde con HTTP 200:
   ```text
   Pedido Recibido y enviado a kafka con ID: 3fa85f64-5717-4562-b3fc-2c963f66afa6
   ```
3. `notification-service` consume el mensaje desde la partición correspondiente y registra en consola:
   ```text
   ==============================================
   Mensaje Recibido desde Kafka
   Particion: [1], Offset: [0]
   ID de orden: 3fa85f64-5717-4562-b3fc-2c963f66afa6
   Cliente: juan.perez@example.com
   Producto: Laptop Lenovo ThinkPad
   Monto: S/. 1250.00
   Estado: PENDING
   Simulando envio de correo/comprobante al cliente...
   ==============================================
   ```
4. Puedes verificar las particiones y el evento persistido en [http://localhost:8085/ui/clusters/local-cluster/topics/order-events](http://localhost:8085/ui/clusters/local-cluster/topics/order-events).
