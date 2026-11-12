<template>
    <div class="view-base">
        <h1 class="page-header pb-10">Testing</h1>
        <section v-if="!showResults" class="grid grid-cols-3 gap-6">
            <div class="w-full rounded-xl bg-surface p-6 shadow-lg sm:p-8 col-span-2">
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
            </div>

            <div class="w-full max-w-3xl rounded-xl bg-surface p-6 shadow-lg sm:p-8">
                <h2 class="text-xl font-bold text-text-base">Attack Types</h2>
                <p class="mt-1 mb-4 text-text-base/70">Select attacks on the image to test its robustness. Attacks are performed separately.</p>
                <p class="mt-1 mb-4 text-secondary">If no attacks are selected, Original PNG will always be enabled.</p>
                <div class="flex flex-col gap-4">
                    <div class="flex flex-row gap-2">
                        <label>Original PNG (No distortion)</label>
                        <ToggleSwitch :model="hasOriginalPng" @toggle="toggleOriginalPng" />
                    </div>
                    <div class="flex flex-row gap-2">
                        <label>JPEG Compression</label>
                        <ToggleSwitch :model="hasJpegCompression" @toggle="toggleJpegCompression" />
                    </div>
                    <div v-if="hasJpegCompression">
                        <button class="secondary-button-base flex flex-row gap-2 mb-2" @click="addJpegQuality">
                            <i class="fi fi-sr-plus text-primary-var self-center text-sm inline-flex"></i>
                            Add Instance
                        </button>
                        <div v-for="(instance, index) in jpegCompressionInstances" class="flex flex-row justify-between border-2 rounded-lg border-primary-light bg-primary-light p-2 my-2">
                            <div class="flex flex-row gap-2">
                                <label class="self-center px-2">Quality: </label>
                                <input type="number" v-model="jpegCompressionInstances[index]" class="input-base w-20" />
                            </div>
                            <button class="cursor-pointer px-2" @click="removeJpegQuality(index)">
                                <i class="fi fi-sr-minus text-error hover:text-error/80 active:text-error/90 inline-flex"></i>
                            </button>
                        </div>
                    </div>
                    <div class="flex flex-row gap-2">
                        <!-- <input type="checkbox" /> -->
                        <label>Gaussian Noise</label>
                        <ToggleSwitch :model="hasGaussianNoise" @toggle="toggleGaussianNoise" />
                    </div>
                    <div v-if="hasGaussianNoise">
                        <div class="flex flex-col gap-4">
                            <div class="flex flex-col gap-1">
                                <label>Mean: {{ gaussianMean }}</label>
                                <input type="range" min="0", max="255" v-model.number="gaussianMean" class="accent-primary cursor-pointer" />
                            </div>
                            <div class="flex flex-col gap-1">
                                <label>Standard Deviation: {{ gaussianStddev }}</label>
                                <input type="range" min="0", max="255" v-model.number="gaussianStddev" class="accent-primary cursor-pointer" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
    <div v-if="effectLoading || loading || resultLoading" class="fixed inset-0 bg-black opacity-50 flex items-center justify-center z-50">
        <i class="fi fi-br-spinner animate-spin text-primary text-9xl"></i>
    </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue';
import type { SelectedImage, TestInfo, TestResults, RobustnessResult } from '@/types/test';
import { useTest } from '@/composables/useTest';
import { useEffects } from '@/composables/useEffects';
import { capitalizeString } from '@/utils/helpers';
import { useRouter } from 'vue-router';
import ToggleSwitch from '@/components/ToggleSwitch.vue';

const router = useRouter()
const { loading, error, runTest, pollTestResults } = useTest()
const { effects, loading: effectLoading, error: effectError, fetchEffects } = useEffects()

const selectedImage = ref<SelectedImage | null>(null);
const expectedPayload = ref('');
const selectedEffect = ref('');
const testInfo = ref<TestInfo>();
const taskId = ref('');
const showResults = ref(false);
const resultLoading = ref(false);

const hasOriginalPng = ref(true);
const hasJpegCompression = ref(false);
const hasGaussianNoise = ref(false);

const jpegCompressionInstances = ref([90, 75, 50, 25]);
const gaussianMean = ref(0);
const gaussianStddev = ref(25);

const noAttacksSelected = computed(() => {
    return !hasJpegCompression.value && !hasGaussianNoise.value
})

onMounted(async () => {
    await fetchEffects()
})

watch(noAttacksSelected, (newValue) => {
    if (newValue) {
        hasOriginalPng.value = true
    }
})

const toggleOriginalPng = () => {
    if (noAttacksSelected.value) {
        return
    }
    hasOriginalPng.value = !hasOriginalPng.value
}

const toggleJpegCompression = () => {
    hasJpegCompression.value = !hasJpegCompression.value
    if (noAttacksSelected.value) {

    }
}

const toggleGaussianNoise = () => {
    hasGaussianNoise.value = !hasGaussianNoise.value
}

const addJpegQuality = () => {
    jpegCompressionInstances.value.push(25)
}

const removeJpegQuality = (index: number) => {
    jpegCompressionInstances.value.splice(index, 1)
}

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

        const attack_details = {
            "original_png": hasOriginalPng.value,
            "jpeg_qualities": hasJpegCompression.value ? jpegCompressionInstances.value : [],
            "gaussian_noise": hasGaussianNoise.value,
            "gaussian_mean": gaussianMean.value,
            "gaussian_stddev": gaussianStddev.value
        }

        formData.append("attacks", JSON.stringify(attack_details))
        testInfo.value = await runTest(formData)
        taskId.value = testInfo.value.task_id
        goToResult()
    }
    // if (testInfo.value) {
    //     resultLoading.value = true
    //     const interval = setInterval(async () => {
    //         const results = await pollTestResults(taskId.value);
    //         testResults.value = results
    //         showResults.value = true
    //         switch (results.state) {
    //             case "SUCCESS":
    //                 clearInterval(interval);
    //                 selectedAttack.value = testResults.value?.result?.robustness[0]?.attack || ''
    //                 resultLoading.value = false;
    //                 break;
    //             case "FAILURE":
    //                 resultLoading.value = false;
    //                 showResultsFailed.value = true;
    //                 break;
    //             default:
    //                 break;
    //         }
    //     }, 5000); //poll every 5 seconds
    // }
}

const goToResult = () => {
    router.push({ name: 'test-result', params: { id: taskId.value }})
}

onBeforeUnmount(() => {
    if (selectedImage.value) {
        URL.revokeObjectURL(selectedImage.value.previewUrl);
        selectedImage.value = null
        showResults.value = false
    }
});

</script>