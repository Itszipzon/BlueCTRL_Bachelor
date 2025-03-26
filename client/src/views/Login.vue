<template>
  <div class="login-container">
    <form @submit.prevent="handleLogin" class="login-form">
      <h2>X-connect Login</h2>

      <div class="form-group">
        <label for="username">Username</label>
        <input
          type="text"
          id="username"
          placeholder="Enter username"
          required
        />
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input
          type="password"
          id="password"
          placeholder="Enter password"
          required
        />
      </div>

      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>

      <div v-if="successMessage" class="success-message">
        {{ successMessage }}
      </div>

      <button type="submit" class="login-button" :disabled="isLoading">
        {{ isLoading ? "Logging in..." : "Login" }}
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref } from "vue";

const props = defineProps({
  doLogin: Function,
});

const isLoading = ref(false);
const errorMessage = ref("");
const successMessage = ref("");

const handleLogin = async () => {
  isLoading.value = true;
  errorMessage.value = "";
  successMessage.value = "";

  try {
    const credentials = btoa(`${username.value}:${password.value}`);

    const response = await fetch("/api/bluebox-vessels-minimal", {
      method: "GET",
      headers: {
        Authorization: `Basic ${credentials}`,
        "Content-Type": "application/json",
      },
    });

    if (response.status === 401) {
      errorMessage.value = "Username or password is wrong. Please try again.";
    } else if (response.status === 200) {
      successMessage.value = "Login successful!";

      localStorage.setItem("SESSION", response.value);
      props.doLogin();
    } else {
      const statusText = response.statusText || "Unknown error";
      errorMessage.value = `Login failed with status: ${response.status} - ${statusText}`;
    }
  } catch (err) {
    errorMessage.value = "An unexpected error occurred. Please try again.";
    console.error("Login error:", err);
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.login-container,
.logged-in-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f5f5f5;
  width: 100%;
}

.login-form {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #333;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #666;
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.login-button {
  width: 100%;
  padding: 0.75rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.login-button:hover {
  background-color: #0056b3;
}

.login-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.error-message {
  color: #dc3545;
  text-align: center;
  margin-bottom: 1rem;
  font-size: 0.9rem;
}

.success-message {
  color: #28a745;
  text-align: center;
  margin-bottom: 1rem;
  font-size: 0.9rem;
}

.logged-in-container {
  flex-direction: column;
}

.logged-in-container button {
  margin-top: 1rem;
  padding: 0.75rem 1.5rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logged-in-container button:hover {
  background-color: #0056b3;
}
</style>
