<template>
    <div class="view-base flex justify-center items-center">
        <div class="bg-primary rounded-2xl">
            <div class="p-4 w-lg">
                <form @submit.prevent="submitCredentials" class="flex flex-col">
                    <h1 class="self-center font-bold text-5xl m-8 text-text-base">EffectEmbed</h1>
                    <hr>
                    <h1 class="self-center font-semibold text-3xl m-6 text-text-base">Login</h1>
                    <div class="flex flex-col m-4">
                        <label class="text-md mb-1">Email</label>
                        <input class="input-base" type="text" v-model="email">
                    </div>
                    <div class="flex flex-col m-4">
                        <label class="text-md mb-1">Password</label>
                        <input class="input-base" type="password" v-model="password">
                    </div>
                    <div class="flex flex-row gap-1 m-4">
                        <input class="input-base" type="checkbox" v-model="rememberMe">
                        <label class="text-md">Remember Me</label>
                    </div>
                    <button class="m-2 button-base self-end" type="submit">Login</button>
                </form>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useAuth } from '@/composables/useAuth';
import { isValidEmail } from '@/utils/validations';
import { ref } from 'vue';

const { loading, error, login } = useAuth();

const email = ref('');
const password = ref('');
const rememberMe = ref(false);
const errorMessage = ref('');
const invalidCredentials = ref(false);

const submitCredentials = async () => {
    invalidCredentials.value = false;
    if (!isValidEmail(email.value)) {
        invalidCredentials.value = true;
        errorMessage.value = 'Invalid email format!';
        return
    }
    const credentials = {
        email: email.value,
        password: password.value,
    }
    console.log(credentials)
    console.log(rememberMe.value)
    await login(credentials, rememberMe.value)
}

</script>