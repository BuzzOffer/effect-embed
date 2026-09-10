export interface StegoLog {
    id: string;
    user_id: string;
    effect_id: string;
    effect_name: string;
    type: string;
    time_taken: number;
    created_at: string;
}

export interface StegoLogListResponse {
    success: boolean;
    message: string;
    data: {
        stego_log: StegoLog[]
    }
}

export interface StegoLogResponse {
    success: boolean;
    message: string;
    data: {
        stego_log: StegoLog
    }
}