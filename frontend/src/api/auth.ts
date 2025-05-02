import axios from "axios";
import TokenService from "./services/tokenService";

export interface Auth {
    accessToken: string;
    refreshToken: string;
}

export interface UserLogin {
    username: string;
    password: string;
}

export interface RefreshToken {
    refreshToken: string;
}

export interface UserRegister {
    username: string;
    password: string;
    email: string;
}

export const refresh = () => {
    const refreshToken: RefreshToken = { refreshToken: TokenService.getRefreshToken()! }
    return axios.post('/auth/refresh', refreshToken)
}



