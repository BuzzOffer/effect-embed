<template>
    <div class="view-base">
        <h1 class="page-header pb-10">Effects</h1>
        <button class="button-base mb-5" @click="openCreate">Create Effect</button>
        <table v-if="!loading && effects.length !== 0"  class="border border-background bg-surface">
            <thead class="bg-primary">
                <tr class="border">
                    <th class="table-cell" rowspan="2">ID</th>
                    <th class="table-cell" rowspan="2">Name</th>
                    <th class="table-cell" colspan="2">Average Processing Time (ms)</th>
                    <th class="table-cell" colspan="2">Total Usage</th>
                    <th class="table-cell" rowspan="2">Average Results</th>
                    <th class="table-cell" rowspan="2">Created At</th>
                    <th class="table-cell" rowspan="2">Actions</th>
                </tr>
                <tr class="border">
                    <th class="table-cell">Embed</th>
                    <th class="table-cell">Extract</th>
                    <th class="table-cell">Embed</th>
                    <th class="table-cell">Extract</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="log in effects" :key="log.id" class="border">
                    <td class="table-cell">{{ log.id }}</td>
                    <td class="table-cell">{{ capitalizeString(log.name) }}</td>
                    <td class="table-cell">{{ log.avg_embed_time }}</td>
                    <td class="table-cell">{{ log.avg_extract_time }}</td>
                    <td class="table-cell">{{ log.total_embed_usage }}</td>
                    <td class="table-cell">{{ log.total_extract_usage }}</td>
                    <td class="table-cell">{{ log.avg_results || 'N/A' }}</td>
                    <td class="table-cell">{{ formatTimestamp(log.created_at) }}</td>
                    <td class="table-cell">
                        <div class="flex flex-row gap-2">
                            <button class="cursor-pointer" @click="openEdit(log.id, log.name)">
                                <i class="fi fi-rr-pencil text-secondary hover:text-secondary/80 active:text-secondary/90"></i>
                            </button>
                            <button class="cursor-pointer" @click="openDelete(log.id)">
                                <i class="fi fi-rs-trash text-error hover:text-error/80 active:text-error/90"></i>
                            </button>
                        </div>
                        
                    </td>
                </tr>
            </tbody>
        </table>
        <div v-else>
            <p>No effects found.</p>
        </div>
    </div>
    <dialog ref="createDialog" class="dialog-base">
        <h1 class="text-2xl font-bold m-2">Create New Effect</h1>
        <div class="flex flex-col m-2 mt-4">
            <label class="text-md mb-1">Effect Name</label>
            <input class="input-base" type="text" v-model="effectName">
        </div>
        <div class="mt-6 flex justify-end gap-3">
            <button class="secondary-button-base" @click="cancelCreate">Cancel</button>
            <button class="button-base" @click="handleCreate">Create</button>
        </div>
    </dialog>
    <dialog ref="editDialog" class="dialog-base">
        <h1 class="text-2xl font-bold m-2">Edit Effect</h1>
        <div class="flex flex-col m-2 mt-4">
            <label class="text-md mb-1">Effect Name</label>
            <input class="input-base" type="text" v-model="effectName">
        </div>
        <div class="mt-6 flex justify-end gap-3">
            <button class="secondary-button-base" @click="cancelEdit">Cancel</button>
            <button class="button-base" @click="handleSave">Save Changes</button>
        </div>
    </dialog>
    <!-- <dialog ref="deleteDialog" class="justify-self-center self-center rounded-xl">
        <div class="bg-slate-50 p-10 rounded-xl">
            <h1 class="text-2xl font-bold m-4">Are you sure you want to delete this effect?</h1>
            <p class="text-lg mx-4 my-2">The changes cannot be undone.</p>
            <div class="flex flex-row justify-end gap-4">
                <button class="secondary-button-base" @click="cancelDelete">No</button>
                <button class="red-button-base" @click="handleDelete">Yes</button>
            </div>
        </div>
    </dialog> -->
    <DialogBox v-model="showDeleteDialog" title="Delete Effect" message="Are you sure you want to delete this effect? The changes cannot be undone." confirm-label="Delete" @confirm="handleDelete" />

    <div v-if="loading" class="fixed inset-0 bg-black opacity-50 flex items-center justify-center z-50">
        <i class="fi fi-br-spinner animate-spin text-blue-800 text-9xl"></i>
    </div>
</template>

<script setup lang="ts">
import DialogBox from '@/components/DialogBox.vue';
import { useEffects } from '@/composables/useEffects';
import { capitalizeString, formatTimestamp } from '@/utils/helpers';
import { onMounted, ref } from 'vue';

const { effects, loading, error, fetchEffects, createEffect, updateEffect, deleteEffect } = useEffects()

const createDialog = ref<HTMLDialogElement | null>(null)
const editDialog = ref<HTMLDialogElement | null>(null)
const showErrorCreate = ref(false)
const showErrorEdit = ref(false)
const deleteDialog = ref<HTMLDialogElement | null>(null)
const showDeleteDialog = ref(false)
const deletingEffectId = ref<string>('')
const effectName = ref('')
const editingEffectId = ref('')

onMounted(async () => {
    await fetchEffects()
})

const openCreate = () => {
    createDialog.value?.showModal()
}

const cancelCreate = () => {
    effectName.value = ''
    createDialog.value?.close()
}

const handleCreate = async () => {
    if (effectName.value === '') {
        showErrorCreate.value = true
        return
    }
    await createEffect({
        name: effectName.value.toLowerCase()
    })
    await fetchEffects()
    cancelCreate()
}

const openEdit = (id: string, name: string) => {
    editingEffectId.value = id
    effectName.value = name
    editDialog.value?.showModal()
}

const cancelEdit = () => {
    editingEffectId.value = ''
    effectName.value = ''
    editDialog.value?.close()
}

const handleSave = async () => {
    if (effectName.value === '') {
        showErrorEdit.value = true
        return
    }
    await updateEffect(editingEffectId.value, {
        name: effectName.value.toLowerCase()
    })
    await fetchEffects()
    cancelEdit()
}

const openDelete = (id: string) => {
    deletingEffectId.value = id
    showDeleteDialog.value = true
}

const cancelDelete = () => {
    deletingEffectId.value = ''
    showDeleteDialog.value = false
}

const handleDelete = async () => {
    await deleteEffect(deletingEffectId.value)
    await fetchEffects()
    cancelDelete()
}

</script>