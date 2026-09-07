import { EffectService } from "@/services/effectService"
import type { Effect, EffectCreation } from "@/types/effect"
import { ref } from "vue"

export function useEffects() {
    const effects = ref<Effect[]>([])

    const loading = ref<boolean>(false)
    const error = ref<string>('')

    const effectService = new EffectService()

    const handleError = (err: any) => {
        error.value = err instanceof Error ? err.message: 'An unexpected error occured';
        console.error('Effect Log API error: ', err)
    }

    const fetchEffects = async (): Promise<void> => {
        try {
            loading.value = true;
            const response = await effectService.getEffectList()

            effects.value = response.data.effects

            if (response.data.effects.length === 0 && response.message) {
                error.value = response.message
            }
        } catch (error) {
            handleError(error)
        } finally {
            loading.value = false;
        }
    }

    const createEffect = async (effect: EffectCreation): Promise<void> => {
        try {
            loading.value = true;
            const response = await effectService.createEffect(effect)


            if (!response.data && response.message) {
                error.value = response.message
            }
        } catch (error) {
            handleError(error)
        } finally {
            loading.value = false;
        }
    }

    const updateEffect = async (id: string, effect: EffectCreation): Promise<void> => {
        try {
            loading.value = true;
            const response = await effectService.updateEffect(id, effect)


            if (!response.data && response.message) {
                error.value = response.message
            }
        } catch (error) {
            handleError(error)
        } finally {
            loading.value = false;
        }
    }


    const deleteEffect = async (id: string): Promise<void> => {
        try {
            loading.value = true;
            const response = await effectService.deleteEffect(id)

            if (!response.data && response.message) {
                error.value = response.message
            }
        } catch (error) {
            handleError(error)
        } finally {
            loading.value = false;
        }
    }

    return {
        effects,
        loading,
        error,

        fetchEffects,
        createEffect,
        updateEffect,
        deleteEffect,
    }
}