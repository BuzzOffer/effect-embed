export interface UserInfo {
    id: string,
    username: string,
    email: string,
    role: string
}

export interface LoginCredentials {
    email: string,
    password: string
}

export interface RegisterCredentials {
    username: string,
    email: string,
    password: string,
    role: string
}

export interface LoginResponse {
    success: boolean,
    message: string,
    data: {
        user: UserInfo,
        token: string
    }
}