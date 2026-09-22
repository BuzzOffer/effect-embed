<template>
    <div class="view-base">
        <h1 class="page-header pb-10">Stego Logs</h1>
        <div class="flex flex-row gap-3 mb-5">
            <select v-model="selectedStegoType" class="input-base w-sm"">
                <option value="">All Types</option>
                <option v-for="type in stegoTypes" :value="type">{{ capitalizeString(type) }}</option>
            </select>
            <select v-model="selectedEffect" class="input-base w-sm"">
                <option value="">All Effects</option>
                <option v-for="effect in effects" :value="effect.name">{{ capitalizeString(effect.name) }}</option>
            </select>
        </div>
        <table v-if="!loading && filteredLogs.length !== 0"  class="border border-background bg-surface">
            <thead class="bg-primary">
                <tr class="border">
                    <th class="table-cell">ID</th>
                    <th class="table-cell">User ID</th>
                    <th class="table-cell">Effect</th>
                    <th class="table-cell">Type</th>
                    <th class="table-cell">Time Taken (ms)</th>
                    <th class="table-cell">Created At</th>
                    <th class="table-cell">Actions</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="log in filteredLogs" :key="log.id" class="border border-slate-50">
                    <td class="table-cell">{{ log.id }}</td>
                    <td class="table-cell">{{ log.user_id }}</td>
                    <td class="table-cell">{{ capitalizeString(log.effect_name) }}</td>
                    <td class="table-cell">{{ capitalizeString(log.type) }}</td>
                    <td class="table-cell">{{ log.time_taken }}</td>
                    <td class="table-cell">{{ formatTimestamp(log.created_at) }}</td>
                    <td class="table-cell">
                        <button class="cursor-pointer" @click="openDelete(log.id)">
                            <i class="fi fi-rs-trash text-error hover:text-error/80 active:text-error/90"></i>
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
        <div v-else>
            <p>No stego logs found.</p>
        </div>
    </div>
    <!-- <dialog ref="deleteDialog" class="justify-self-center self-center rounded-xl">
        <div class="bg-surface p-10 rounded-xl">
            <h1 class="text-2xl font-bold m-4">Are you sure you want to delete this stego log?</h1>
            <p class="text-lg mx-4 my-2">The changes cannot be undone.</p>
            <div class="flex flex-row justify-end gap-4">
                <button class="secondary-button-base" @click="cancelDelete">No</button>
                <button class="red-button-base" @click="handleDelete">Yes</button>
            </div>
        </div>
    </dialog> -->
    <DialogBox v-model="showDeleteDialog" variant="confirm" title="Delete Stego Log" message="Are you sure you want to delete this stego log? The changes cannot be undone." confirm-label="Delete" @confirm="handleDelete" />

    <div v-if="loading" class="fixed inset-0 bg-black opacity-50 flex items-center justify-center z-50">
        <i class="fi fi-br-spinner animate-spin text-primary text-9xl"></i>
    </div>
</template>

<script setup lang="ts">
import DialogBox from '@/components/DialogBox.vue';
import { useEffects } from '@/composables/useEffects';
import { useStegoLog } from '@/composables/useStegoLog';
import { capitalizeString, formatTimestamp } from '@/utils/helpers';
import { onMounted, ref, computed } from 'vue';

const { stego_logs, stego_log, loading, error, fetchStegoLogs, deleteStegoLog } = useStegoLog()
const { effects, fetchEffects } = useEffects()

// const deleteDialog = ref<HTMLDialogElement | null>(null)
const showDeleteDialog = ref(false)
const deletingLogId = ref<string>('')
const selectedStegoType = ref('')
const selectedEffect = ref('')
const stegoTypes = ref(['embed', 'extract'])

const filteredLogs = computed(() => {
    let filtered = [...stego_logs.value]

    if (selectedStegoType.value !== '') {
        filtered = filtered.filter(log => log.type === selectedStegoType.value)
    }

    if (selectedEffect.value !== '') {
        filtered = filtered.filter(log => log.effect_name === selectedEffect.value)
    }

    return filtered
})

onMounted(async () => {
    await fetchStegoLogs()
    await fetchEffects()
})

const openDelete = (id: string) => {
    deletingLogId.value = id
    showDeleteDialog.value = true
}

const cancelDelete = () => {
    deletingLogId.value = ''
    showDeleteDialog.value = false
}

const handleDelete = async () => {
    await deleteStegoLog(deletingLogId.value)
    await fetchStegoLogs()
    cancelDelete()
}


</script>