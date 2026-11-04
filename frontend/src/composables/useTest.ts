import { TestService } from "@/services/testService"
import type { TestInfo, TestResults } from "@/types/test";
import { ref } from "vue";

export function useTest() {
    const loading = ref<boolean>(false)
    const error = ref<string>('')

    const testService = new TestService()

    const handleError = (err: any) => {
        error.value = err instanceof Error ? err.message: 'An unexpected error occured';
        console.error('Test API error: ', err)
    }

    const runTest = async (imageData: FormData): Promise<TestInfo> => {
        try {
            loading.value = true
            const response = await testService.runTestService(imageData)
            return response
        } catch (error) {
            handleError(error)
            return {
                task_id: "",
                status: "An error occurred"
            }
        } finally {
            loading.value = false
        }
    }

    const pollTestResults = async (id: string): Promise<TestResults> => {
        try {
            loading.value = true
            const response = await testService.pollTestResultsService(id)
            return response;
        } catch (error) {
            handleError(error)
            return {
                "state": 'ERROR'
            }
        } finally {
            loading.value = false
        }
    }

    return {
        loading,
        error,

        runTest,
        pollTestResults
    }
}