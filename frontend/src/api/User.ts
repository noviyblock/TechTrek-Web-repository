import axiosInstance from "./AxiosInstance"
import UserService, { User } from "./services/UserService"

export const getUser = async (): Promise<User> => {
    return (await axiosInstance.get<User>("/api/user/me", {params: { username: UserService.getUsername()!}})).data
}
