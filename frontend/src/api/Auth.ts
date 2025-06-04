import axios from "axios";
import TokenService from "./services/TokenService";
import axiosInstance, { safeRequest } from "./AxiosInstance";
import UserService from "./services/UserService";

export interface Auth {
  accessToken: string;
  refreshToken: string;
}

export interface UserLogin {
  email: string;
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

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  username: string;
}

export interface StatusResponse {
  authorized: boolean;
  user: {
    id: number;
    name: string;
  };
}

export interface OtpRequest {
  email: string;
  otp: string;
}

export const refresh = () => {
  const refreshToken: RefreshToken = {
    refreshToken: TokenService.getRefreshToken()!,
  };
  return axios.post("/auth/refresh", refreshToken);
};

export const register = async (userRegister: UserRegister) => {
  try {
    const response = await axios.post<AuthResponse>(
      "/auth/register",
      userRegister
    );
    TokenService.setTokens(response.data);
    UserService.setUsername(response.data.username);
    return response.status
  } catch (error) {
    console.error(error);
    // todo handle error
  }
};

export const login = async (userLogin: UserLogin) => {
  try {
    const response = await axios.post<AuthResponse>(
      "/auth/login",
      userLogin
    );
    TokenService.setTokens(response.data);
    UserService.setUsername(response.data.username);
    return response.status;
  } catch (error) {
    //todo handle error
  }
};

export const guest = async() => {
  try {
    const response = await axios.post<AuthResponse>(
      "/auth/guest"
    );
    TokenService.setTokens(response.data);
    UserService.setUsername(response.data.username);
    return response.status;
  } catch (error) {
  }
}

export const status = async (): Promise<boolean> => {
  try {
    return (
      (await axiosInstance.get("/auth/status")).status === 200 && UserService.getUsername() !== "undefined"
    );
  } catch (error) {
    console.error(error);
    return false;
  }
};

export const verify_otp = async ({email, otp}: OtpRequest) => safeRequest<string>(() => axiosInstance.post("/verify-otp", {email, otp}));