import axiosInstance from "./AxiosInstance";

export interface Sphere {
    id: number;
    name: string;
}

export interface Mission {
    id: number;
    name: string;
    sphere: Sphere;
}

interface StartGame {
    missionId: number;
    companyName: string;
}

export interface GameState {
    gameId: number;
    companyName: string;
    stage: number;
    turnNumber: number;
    monthsPassed: number;
    missionId: number;

    money: number;
    technicReadiness: number;
    productReadiness: number;
    motivation: number;
    
    juniors: number;
    middles: number;
    seniors: number;

    superEmployees: string[];
    numberOfOffices: number;

    situationText: string;
}

interface DecisionRequest {
    decision: string;
}

export interface DecisionResponse {    
    motivationDelta: number;
    newMotivation: number;
    technicalDelta: number;
    newTechnicalReadiness: number;
    productDelta: number;
    newProductReadiness: number;
    moneyDelta: number;
    newMoney: number;

    textToPlayer: string;

    qualityScore: number;
    roll: {
        diceTotal: number,
        zone: string,
    }
}

export const getSpheres = async () => {
    return (await axiosInstance.get("/api/game/spheres")).data;
}

export const getMissions = async (sphereId: number, page: number = 0, size: number = 3) => {
    return (await axiosInstance.get<Mission[]>("/api/missions", { params: {sphereId, page, size} })).data;
}

export const startGame = async (req: StartGame) => {
    return (await axiosInstance.post<GameState>("/api/game/start", req)).data
}

export const evaluateDesicion = async (gameId: number, req: string) => {
    return (await axiosInstance.post<DecisionResponse>(`/api/game/${gameId}/evaluate-decision`, {decision: req})).data
}

export const state = async (gameId: number) => {
    return (await axiosInstance.get<GameState>(`/api/game/${gameId}/state`)).data
}