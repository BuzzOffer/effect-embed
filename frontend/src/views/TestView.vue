<template>
    <div class="view-base">
        <h1 class="page-header pb-10">Testing</h1>
        <section v-if="showResults" class="w-full rounded-xl bg-surface p-6 shadow-lg sm:p-8">
            <div class="mb-6">
                <h2 class="text-xl font-bold text-text-base">Test Results</h2>
                <p class="mt-1 text-text-base/70">Robustness test results for the embedded image.</p>
            </div>

            <div v-if="showResultsFailed" class="flex flex-row gap-4 mb-6 justify-between bg-secondary w-full rounded-xl p-4 shadow-lg sm:p-8">
                <h3 class="text-lg font-bold text-error">Robustness Test Failed</h3>
                <p class="mt-1 text-text-base/70">{{ testResults?.error }}</p>
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
        <section v-if="!showResults" class="w-full max-w-3xl rounded-xl bg-surface p-6 shadow-lg sm:p-8">
            <div class="mb-6">
                <h2 class="text-xl font-bold text-text-base">Robustness Test</h2>
                <p class="mt-1 text-text-base/70">Select an embedded image and enter the expected payload.</p>
            </div>

            <div class="flex flex-col gap-6">
                <div v-if="!selectedImage">
                    <label class="mb-2 block text-md font-bold text-text-base" for="image-picker">Select Image</label>
                    <label
                        for="image-picker"
                        class="flex min-h-44 cursor-pointer flex-col items-center justify-center rounded-xl border-2 border-dashed border-primary-light p-6 text-center transition-colors hover:bg-primary-light/40"
                    >
                        <i class="fi fi-sr-picture text-4xl text-secondary"></i>
                        <span class="mt-3 font-bold text-text-base">Choose image</span>
                        <span class="mt-1 text-sm text-text-base/70">PNG or JPEG</span>
                        <input
                            id="image-picker"
                            class="sr-only"
                            type="file"
                            accept="image/*"
                            @change="handleImageSelection"
                        >
                    </label>
                </div>

                <div v-if="selectedImage" class="grid grid-cols-2 gap-3 sm:grid-cols-3">
                    <div class="group relative overflow-hidden rounded-lg border-2 border-primary-light bg-background">
                        <img :src="selectedImage.previewUrl" :alt="selectedImage.file.name" class="aspect-square w-full object-cover">
                        <button
                            type="button"
                            class="cursor-pointer absolute right-2 top-2 flex h-8 w-8 items-center justify-center rounded-full bg-background/90 text-error opacity-0 transition-opacity group-hover:opacity-100 focus:opacity-100"
                            :aria-label="`Remove ${selectedImage.file.name}`"
                            @click="removeImage"
                        >
                            <i class="fi fi-sr-cross-small"></i>
                        </button>
                        <p class="truncate px-2 py-1 text-sm text-text-base" :title="selectedImage.file.name">{{ selectedImage.file.name }}</p>
                    </div>
                </div>

                <div class="flex flex-col">
                    <label class="mb-2 text-md font-bold text-text-base" for="expected-payload">Effect Used</label>
                    <select v-model="selectedEffect" class="input-base w-sm" :class="selectedEffect ? '' : 'text-gray-500'">
                        <option value="" disabled>Effect Type</option>
                        <option v-for="effect in effects" :value="effect.name" class="text-black">{{ capitalizeString(effect.name) }}</option>
                    </select>
                </div>

                <div class="flex flex-col">
                    <label class="mb-2 text-md font-bold text-text-base" for="expected-payload">Expected Payload</label>
                    <textarea
                        id="expected-payload"
                        v-model="expectedPayload"
                        class="input-base min-h-32 resize-y"
                        placeholder="Enter expected payload text"
                    ></textarea>
                </div>

                <div class="flex justify-end">
                    <button @click="handleRunTest" type="button" class="button-base" :disabled="!selectedImage || !expectedPayload.trim()">
                        Run Test
                    </button>
                </div>
            </div>
        </section>
    </div>
    <div v-if="effectLoading || loading || resultLoading" class="fixed inset-0 bg-black opacity-50 flex items-center justify-center z-50">
        <i class="fi fi-br-spinner animate-spin text-primary text-9xl"></i>
    </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue';
import type { SelectedImage, TestInfo, TestResults, RobustnessResult } from '@/types/test';
import { useTest } from '@/composables/useTest';
import { useEffects } from '@/composables/useEffects';
import { capitalizeString } from '@/utils/helpers';

const { loading, error, runTest, pollTestResults } = useTest()
const { effects, loading: effectLoading, error: effectError, fetchEffects } = useEffects()

const selectedImage = ref<SelectedImage | null>(null);
const expectedPayload = ref('');
const selectedEffect = ref('');
const testInfo = ref<TestInfo>();
const taskId = ref('');
const showResults = ref(false);
const testResults = ref<TestResults>();
const selectedAttack = ref('')
const resultLoading = ref(false);
const showResultsFailed = ref(false);

const selectedResult = computed(() => {
    return testResults.value?.result?.robustness.find(
        result => result.attack === selectedAttack.value
    )
})

onMounted(async () => {
    await fetchEffects()
})

const handleImageSelection = (event: Event) => {
    const input = event.target as HTMLInputElement;
    const file = input.files ? input.files[0] : null;

    if (!file) {
        return
    }

    selectedImage.value = {
        id: `${file.name}-${file.lastModified}-${Date.now()}`,
        file,
        previewUrl: URL.createObjectURL(file),
    }

    input.value = '';
};

const removeImage = () => {
    if (!selectedImage.value) {
        return
    }
    URL.revokeObjectURL(selectedImage.value.previewUrl);
    selectedImage.value = null
};

const handleRunTest = async () => {
    if (selectedImage.value) {
        const formData = new FormData()
        formData.append("stego_image", selectedImage.value.file)
        formData.append("expected_payload", expectedPayload.value)
        formData.append("effect_type", selectedEffect.value)

        testInfo.value = await runTest(formData)
        taskId.value = testInfo.value.task_id
    }
    if (testInfo.value) {
        resultLoading.value = true
        const interval = setInterval(async () => {
            const results = await pollTestResults(taskId.value);
            testResults.value = results
            showResults.value = true
            switch (results.state) {
                case "SUCCESS":
                    clearInterval(interval);
                    selectedAttack.value = testResults.value?.result?.robustness[0]?.attack || ''
                    resultLoading.value = false;
                    break;
                case "FAILURE":
                    resultLoading.value = false;
                    showResultsFailed.value = true;
                    break;
                default:
                    break;
            }
        }, 5000); //poll every 5 seconds
    }
}

onBeforeUnmount(() => {
    if (selectedImage.value) {
        URL.revokeObjectURL(selectedImage.value.previewUrl);
        selectedImage.value = null
        showResults.value = false
    }
});

</script>