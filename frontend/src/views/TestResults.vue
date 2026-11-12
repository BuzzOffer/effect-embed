<template>
    <section class="w-full rounded-xl bg-surface p-6 shadow-lg sm:p-8">
        <div class="mb-6">
            <h2 class="page-header font-bold text-text-base">Test Results</h2>
            <p class="mt-1 text-text-base/70">Robustness test results for the embedded image.</p>
        </div>

        <div v-if="showResultsFailed" class="flex flex-row gap-4 mb-6 justify-between bg-secondary w-full rounded-xl p-4 shadow-lg sm:p-8">
            <h3 class="text-xl font-bold text-error">Robustness Test Failed</h3>
            <p class="mt-1 text-text-base/70">{{ testResults?.error || "No test results found" }}</p>
        </div>

        <div v-else class="flex flex-row gap-4 mb-6 justify-between bg-secondary w-full rounded-xl p-4 shadow-lg sm:p-8">
            <div>
                <h3 class="text-lg font-bold text-text-base">Total Robustness Score</h3>
                <p class="mt-1 text-text-base/70">Successful tests out of 4</p>
            </div>
            <h1 class="font-bold text-4xl self-center">{{ testResults?.result?.robustness_score }}</h1>
        </div>

        <div class="mb-6 bg-secondary w-full rounded-xl p-4 shadow-lg sm:p-8">
            <h3 class="text-lg font-bold text-text-base">Expected Payload</h3>
            <p class="mt-1 text-text-base/70">{{ testResults?.result?.expected_payload }}</p>
        </div>
            
        <h2 class="text-lg font-bold text-text-base mb-6">Attack Types</h2>

        <div role="tablist" class="w-full">
            <button v-for="result in testResults?.result?.robustness" 
                :key="result.attack"
                type="button" 
                role="tab" 
                class="px-4 py-2 font-semibold cursor-pointer mb-4" 
                :class="selectedAttack === result.attack ? 'border-b-2 border-primary text-primary' : 'text-text-base/60'" 
                @click="selectedAttack = result.attack"
            >
                {{ result.attack }}
            </button>
        </div>

        <div v-if="selectedResult" class="flex flex-col gap-6">
            <h2 class="text-lg">{{ selectedResult.details }}</h2>
            <div class="flex flex-row justify-between bg-primary-light w-full rounded-xl p-4 shadow-lg sm:p-8">
                <div>
                    <h3 class="text-lg font-bold text-text-base">Bit Error Rate (BER)</h3>
                    <p class="mt-1 text-text-base/70">The Bit Error Rate of the extracted payload</p>
                </div>
                <h1 class="font-bold text-4xl self-center">{{ selectedResult.ber }}</h1>
            </div>
            <div class="bg-primary-light w-full rounded-xl p-4 shadow-lg sm:p-8">
                <div>
                    <h3 class="text-lg font-bold text-text-base">Decoded Payload</h3>
                    <p class="mt-1" :class="!selectedResult.decoded_payload ? 'text-error' : 'text-text-base/70'">{{ selectedResult.decoded_payload || "No Payload Survived"}}</p>
                </div>
            </div>
        </div>
    </section>
    <div v-if="loading || resultLoading" class="fixed inset-0 bg-black opacity-50 flex items-center justify-center z-50">
        <i class="fi fi-br-spinner animate-spin text-primary text-9xl"></i>
    </div>
</template>

<script setup lang="ts">
import { useTest } from '@/composables/useTest';
import type { TestResults } from '@/types/test';
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute()

const testResults = ref<TestResults>()
const selectedAttack = ref('')
const resultLoading = ref(false);
const showResultsFailed = ref(false);

const props = defineProps<{
    id: string
}>()

const selectedResult = computed(() => {
    return testResults.value?.result?.robustness.find(
        result => result.attack === selectedAttack.value
    )
})

const taskId = computed(() => props.id)
const { loading, error, pollTestResults } = useTest()

onMounted(() => {
    if (!taskId.value) {
        showResultsFailed.value = true
        return
    }
    resultLoading.value = true;
    const interval = setInterval(async () => {
        const results = await pollTestResults(taskId.value);
        testResults.value = results
        switch (results.state) {
            case "SUCCESS":
                clearInterval(interval);
                selectedAttack.value = testResults.value?.result?.robustness[0]?.attack || ''
                resultLoading.value = false;
                break;
            case "FAILURE":
                clearInterval(interval);
                resultLoading.value = false;
                showResultsFailed.value = true;
                break;
            default:
                break;
        }
    }, 5000); //poll every 5 seconds
})
</script>