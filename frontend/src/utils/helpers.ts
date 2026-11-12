export const formatTimestamp = (timestamp: string | Date | null | undefined): string => {
    if (!timestamp) {
        return ''
    }
    const date = typeof timestamp === 'string' ? new Date(timestamp) : timestamp
    if (isNaN(date.getTime())) {
        return ''
    }
    const locale = 'en-MY'
    const options: Intl.DateTimeFormatOptions = {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
    }
    return date.toLocaleString(locale, options).replace(',', ' ')
}

export const formatTimestampToDate = (timestamp: string | null | undefined): string => {
    if (timestamp) {
        const formattedTs = formatTimestamp(timestamp).slice(0, 10)
        return formattedTs
    } else {
        return ''
    } 
}

export const capitalizeString = (str: string): string => {
    if (!str) return '';
    if (str.includes(' ')) {
        return str.split(' ').map(word => capitalizeString(word)).join(' ')
    }
    const capitalizedString: string = str.charAt(0).toUpperCase() + str.slice(1)
    return capitalizedString
}

export const snakeToTitle = (str: string): string => {
    return str.split('_').map(word => capitalizeString(word)).join(' ')
}

export const toSnakeCase = (str: string): string => {
    return str.toLowerCase().replace(/ /g, "_")
}

export const isTokenExpired = (token: string): boolean => {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url?.replace(/-/g, '+').replace(/_/g, '/');

        if (!base64) {
            return true;
        }
        
        const payload = JSON.parse(atob(base64));
        if (!payload.exp) {
            return true;
        }

        const currentTime = Date.now() / 1000;
        return payload.exp < currentTime;
    } catch {
        return true;
    }
}