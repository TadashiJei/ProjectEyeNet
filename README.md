# 🚀 Node.js-Based SMS Gateway with ESP32 + GSM

## 📚 **Project Overview**
This project is a full-stack **SMS Gateway Web Application** built with **Node.js, Express, Next.js, MongoDB, and ESP32 (with GSM module SIM800L)**. It allows authenticated users to send and receive SMS via a user-friendly web dashboard. ESP32 devices can interact securely with the backend using **custom API keys**. The system is scalable, secure, and ready for deployment on cloud platforms like AWS, DigitalOcean, or Vercel.

---

## 📝 **Features**

### 🌐 **Frontend (Next.js)**
- **Authentication System:**
   - User login and registration.
   - OTP verification via **Mailgun API**.
   - Password reset functionality.
- **Dashboard:**
   - User-friendly SMS sending interface.
   - View and manage incoming SMS logs.
   - Monitor SMS delivery status.
- **User Profile:**
   - API key generation and management.
   - Update personal information.
- **Admin Panel:**
   - Manage users and roles.
   - View SMS logs and system activity.
   - Revoke or regenerate API keys.
- **Responsive UI:** Built with **ShadCN** and **TailwindCSS** for a seamless experience across devices.

### 🛠️ **Backend (Node.js + Express)**
- **Authentication:**
   - JWT-based session management.
   - Role-based access control (User/Admin).
- **SMS Endpoints:**
   - API endpoints to send and receive SMS.
   - WebSocket support for real-time SMS updates.
- **API Key Management:**
   - Generate and validate custom API keys for ESP32 devices.
   - Middleware for API key validation.
- **Logs & Monitoring:**
   - Store SMS logs, errors, and activity history in MongoDB.
   - Provide logs via API and admin dashboard.
- **Security:**
   - Input validation and sanitization.
   - Rate limiting for sensitive endpoints.
   - Environment variable management with `dotenv`.

### 📲 **ESP32 Firmware**
- **Wi-Fi Connectivity:**
   - Connect ESP32 to Wi-Fi using `WiFi.h`.
- **HTTP API Communication:**
   - Send HTTP requests to backend endpoints.
   - Parse JSON responses.
- **SMS Control via GSM Module:**
   - Use AT commands to send and receive SMS.
   - Handle error codes and retries.
- **Custom API Key Integration:**
   - Include API keys in HTTP headers for validation.
- **Error Handling:**
   - Retry failed HTTP requests.
   - Log connectivity errors via Serial Monitor.

### ☁️ **Deployment**
- **Backend:** AWS EC2 / DigitalOcean.
- **Frontend:** Vercel.
- **Database:** MongoDB Atlas.
- **CI/CD:** GitHub Actions for automated deployments.

### 🔒 **Security Best Practices**
- JWT Authentication for secure sessions.
- API Rate Limiting to prevent abuse.
- API Key-based ESP32 authentication.
- Use `.env` for managing sensitive configurations.

---

## 📂 **Folder Structure**
```
📦 sms-gateway
├── 📁 backend
│   ├── controllers
│   ├── models
│   ├── routes
│   ├── middleware
│   ├── config
│   ├── server.js
│
├── 📁 frontend
│   ├── pages
│   ├── components
│   ├── utils
│   ├── styles
│
├── 📁 esp32
│   ├── main.cpp
│   ├── config.h
│
├── 📄 README.md
└── 📄 .env
```

---

## 🔑 **Environment Variables (.env)**
Configure your `.env` file for both backend and frontend:
```
# Backend
PORT=3000
MONGODB_URI=<your-mongodb-uri>
JWT_SECRET=<your-jwt-secret>
MAILGUN_API_KEY=<your-mailgun-api-key>
MAILGUN_DOMAIN=<your-mailgun-domain>

# ESP32 API
ESP32_API_KEY=<your-custom-api-key>
```

---

## 🚀 **Getting Started**

### 1️⃣ **Backend Setup**
```bash
cd backend
npm install
node server.js
```

### 2️⃣ **Frontend Setup**
```bash
cd frontend
npm install
npm run dev
```

### 3️⃣ **ESP32 Firmware Upload**
- Install **Arduino IDE**.
- Install libraries: `HTTPClient`, `WiFi`, `ArduinoJson`.
- Upload firmware to ESP32.

### 4️⃣ **Run the Project**
- Backend: `http://localhost:3000`
- Frontend: `http://localhost:3001`
- ESP32: Connect via API.

---

## 📊 **Project Phases**

### 📌 **Phase 1: Project Initialization**
- Set up project repositories.
- Configure MongoDB and environment variables.
- Implement backend authentication.

### 📌 **Phase 2: Backend API Development**
- Create endpoints for sending and receiving SMS.
- Implement API key middleware.
- Develop SMS log management.

### 📌 **Phase 3: Frontend Development**
- Build login and registration pages.
- Create the SMS dashboard.
- Design the admin panel.

### 📌 **Phase 4: ESP32 Integration**
- Develop firmware to interact with the backend.
- Enable SMS sending via AT commands.
- Implement retry mechanisms.

### 📌 **Phase 5: Testing and Debugging**
- Test API endpoints using **Postman**.
- Verify ESP32 connectivity and GSM functionality.
- Perform frontend UI/UX testing.

### 📌 **Phase 6: Deployment and CI/CD**
- Deploy backend on **AWS EC2/DigitalOcean**.
- Deploy frontend on **Vercel**.
- Set up CI/CD pipelines.

### 📌 **Phase 7: Documentation and Final Review**
- Finalize README and API documentation.
- Conduct a security review.
- Prepare for launch.

---

## 🤖 **AI Prompt for Full Guide Creation**
**"You are an expert technical documentation writer. Create a complete step-by-step guide for building a Node.js-based SMS Gateway application integrated with ESP32 and GSM. Include detailed sections on project setup, backend, frontend, ESP32 firmware, deployment, security best practices, and troubleshooting. Provide examples, code snippets, and configuration instructions. Ensure clarity, logical flow, and practical usability."**

---

## 🤝 **Contributing**
- Fork the repository.
- Create a new branch.
- Submit a pull request.

---

## 📄 **License**
This project is licensed under the **MIT License**.

---

## 📬 **Contact**
For questions or feedback:
- **Email:** hi@tadashijei.com
- **GitHub:** [Your GitHub Profile](https://github.com/TadashiJei)
- **LinkedIn:** [Your LinkedIn Profile](https://linkedin.com/in/javajaybartolome)

---

🚀 **Happy Coding!** 🚀
