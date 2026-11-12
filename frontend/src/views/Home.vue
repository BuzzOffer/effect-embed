<template>
    <div class="view-base">
        <h1 class="page-header mb-10">Welcome Back</h1>
        <div class="grid grid-cols-3 gap-4">
            <div class="bg-surface p-4 w-full h-fit rounded-xl shadow-lg">
                <h2 class="font-semibold text-lg mb-2">Best Performing Effects (Embed)</h2>
                <table class="border border-background bg-surface">
                    <thead class="bg-primary">
                        <tr class="border">
                            <th class="table-cell">Effect</th>
                            <th class="table-cell">Avg Time (ms)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="effect in sortedEmbedEffects" :key="effect.id" class="border">
                            <td class="table-cell">{{ capitalizeString(effect.name) }}</td>
                            <td class="table-cell">{{ effect.avg_embed_time }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="bg-surface p-4 w-full h-fit rounded-xl shadow-lg">
                <h2 class="font-semibold text-lg mb-2">Best Performing Effects (Extract)</h2>
                <table class="border border-background bg-surface">
                    <thead class="bg-primary">
                        <tr class="border">
                            <th class="table-cell">Effect</th>
                            <th class="table-cell">Avg Time (ms)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="effect in sortedExtractEffects" :key="effect.id" class="border">
                            <td class="table-cell">{{ capitalizeString(effect.name) }}</td>
                            <td class="table-cell">{{ effect.avg_extract_time }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useEffects } from '@/composables/useEffects';
import { capitalizeString } from '@/utils/helpers';
import { computed, onMounted, ref } from 'vue';

const { effects, loading, error, fetchEffects } = useEffects()

const sortedEmbedEffects = computed(() => {
    let sorted = [...effects.value]

    sorted = sorted.sort((a, b) => a.avg_embed_time - b.avg_embed_time)
    sorted = sorted.slice(0,3)

    return sorted
})

const sortedExtractEffects = computed(() => {
    let sorted = [...effects.value]

    sorted = sorted.sort((a, b) => a.avg_extract_time - b.avg_extract_time)
    sorted = sorted.slice(0,3)

    return sorted
})

onMounted(async () => {
    await fetchEffects()
})

</script>