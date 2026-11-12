<template>
    <button
        type="button"
        role="switch"
        :aria-checked="model"
        @click="toggle"
        class="relative cursor-pointer inline-flex h-6 w-11 items-center rounded-full transition-colors duration-200 focus:outline-none"
        :class="model ? 'bg-secondary' : 'bg-primary-light'"
    >
        <span
        class="inline-block h-4 w-4 transform rounded-full bg-white transition-transform duration-200"
        :class="model ? 'translate-x-6' : 'translate-x-1'"
        />
    </button>
</template>

<script setup lang="ts">
interface Props {
    model: boolean
}

const { model = false } = defineProps<Props>()
const emit = defineEmits<{
    (e: 'toggle'): void
}>()

const toggle = () => {
    emit('toggle')
}
</script>