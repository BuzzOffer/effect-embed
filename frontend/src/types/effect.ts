export interface Effect {
    id: string;
    name: string;
    avg_embed_time: number;
    avg_extract_time: number;
    total_embed_usage: number;
    total_extract_usage: number;
    avg_results: Record<any, any> | null;
    created_at: string;
}

export interface EffectListResponse {
    success: boolean;
    message: string;
    data: {
        effects: Effect[]
    }
}

export interface EffectResponse {
    success: boolean;
    message: string;
    data: {
        effect: Effect
    }
}

export interface EffectCreation {
    name: string;
}