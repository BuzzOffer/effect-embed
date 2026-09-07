import { StegoLogService } from "@/services/stegoLogService"
import type { StegoLog, StegoLogCreation } from "@/types/stego"
import { ref } from "vue"

export function useStegoLog() {
    const stego_logs = ref<StegoLog[]>([])
    const stego_log = ref<StegoLog>()

    const loading = ref<boolean>(false)
    const error = ref<string>('')

    const stegoLogService = new StegoLogService()

    const handleError = (err: any) => {
        error.value = err instanceof Error ? err.message: 'An unexpected error occured';
        console.error('Stego Log API error: ', err)
    }

    const fetchStegoLogs = async (): Promise<void> => {
        try {
            loading.value = true;
            const response = await stegoLogService.getStegoLogList()

            stego_logs.value = response.data.stego_log

            if (response.data.stego_log.length === 0 && response.message) {
                error.value = response.message
            }
        } catch (error) {
            handleError(error)
        } finally {
            loading.value = false;
        }
    }

    const fetchStegoLogById = async (id: string): Promise<void> => {
        try {
            loading.value = true;
            const response = await stegoLogService.getStegoLogById(id)

            stego_log.value = response.data.stego_log

            if (!response.data && response.message) {
                error.value = response.message
            }
        } catch (error) {
            handleError(error)
        } finally {
            loading.value = false;
        }
    }

    const deleteStegoLog = async (id: string): Promise<void> => {
        try {
            loading.value = true;
            const response = await stegoLogService.deleteStegoLog(id)

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
        stego_logs,
        stego_log,
        loading,
        error,

        fetchStegoLogs,
        fetchStegoLogById,
        deleteStegoLog,
    }
}