<script setup>
/**
 * Import Vue's Composition API and Axios for HTTP requests.
 */
import { ref } from "vue";
import axios from "axios";

/**
 * Props
 * @prop {Function} doLogin - Callback function triggered after successful login.
 */
const props = defineProps({
  doLogin: Function,
});

/**
 * State variables
 * @var {Ref<boolean>} isLoading - Indicates whether the login request is in progress.
 * @var {Ref<string>} errorMessage - Stores any error message to be displayed.
 * @var {Ref<string>} successMessage - Stores a success message when login is successful.
 */
const isLoading = ref(false);
const errorMessage = ref("");
const successMessage = ref("");

/**
 * Local state for form inputs.
 * These should be added to bind input values.
 */
const username = ref("");
const password = ref("");

/**
 * Handles the login form submission.
 * Encodes the username and password in base64, sends it to the login endpoint,
 * and stores the session token in localStorage on success.
 * Calls the `doLogin` function from props on success.
 */
const handleLogin = async () => {
  isLoading.value = true;
  errorMessage.value = "";
  successMessage.value = "";

  const credentials = btoa(`${username.value}:${password.value}`);
  const url = "http://localhost:8080/api/login";

  axios.post(url, {}, {
    headers: {
      Authorization: `Basic ${credentials}`,
    }
  })
  .then((r) => {
    if (r.status === 200) {
      // Save session token and notify parent
      localStorage.setItem("SESSION", credentials);
      props.doLogin();
      successMessage.value = "Login successful!";
    }
  })
  .catch((e) => {
    // Show error message if request fails
    errorMessage.value = "Login failed. Please try again.";
  });

  isLoading.value = false;
};
</script>
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
          v-model="username"
        />
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input
          type="password"
          id="password"
          placeholder="Enter password"
          required
          v-model="password"
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
