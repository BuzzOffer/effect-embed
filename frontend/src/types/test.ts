export type SelectedImage = {
    id: string;
    file: File;
    previewUrl: string;
};

export type TestInfo = {
    task_id: string;
    status: string;
}

export type RobustnessResult = {
    attack: string;
    ber: number;
    decoded_payload: string;
    success: boolean
}

export type TestResults = {
    state: string;
    status?: string;
    error?: string;
    result?: {
        expected_payload: string;
        robustness_score: number;
        robustness: RobustnessResult[]
    }
}