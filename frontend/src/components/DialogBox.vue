<template>
    <dialog ref="dialogRef" class="dialog-base" @cancel.prevent="cancel">
        <h2 class="text-2xl font-bold m-2">
            {{ title }}
        </h2>
        <p class="m-2 mt-4 text-lg">
            <slot>{{ message }}</slot>
        </p>
        <div class="mt-6 flex justify-end gap-3">
            <button v-if="hasCancelButton" type="button" class="secondary-button-base" @click="cancel">
                {{ cancelLabel }}
            </button>
            <button type="button" class="button-base" @click="confirm">
                {{ confirmLabel }}
            </button>
        </div>
    </dialog>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue';

const props = withDefaults(defineProps<{
    modelValue: boolean //checks if dialog is open
    variant?: 'confirm' | 'warning' | 'info'
    title?: string
    message?: string
    confirmLabel?: string
    cancelLabel?: string
    showCancel?: boolean
}>(), {
    variant: 'confirm',
    title: 'Confirm Action',
    message: '',
    confirmLabel: 'OK',
    cancelLabel: 'Cancel'
})

const emit = defineEmits<{
    'update:modelValue': [value: boolean]
    confirm: []
    cancel: []
}>()

const dialogRef = ref<HTMLDialogElement | null>(null)
const hasCancelButton = computed(() => {
    return props.showCancel || props.variant === 'confirm'
})

const closeDialog = () => {
    emit('update:modelValue', false)
}

const confirm = () => {
    emit('confirm')
    closeDialog()
}

const cancel = () => {
    emit('cancel')
    closeDialog()
}

const updateDialog = (isOpen: boolean) => {
    if (!dialogRef.value) return

    if (isOpen && !dialogRef.value.open) {
        dialogRef.value.showModal()
    } else if (!isOpen && dialogRef.value.open) {
        dialogRef.value.close()
    }
}

watch (() => props.modelValue, updateDialog)

onMounted(() => {
    updateDialog(props.modelValue)
})

</script>