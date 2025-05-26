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

  finalScore: FinalScore;
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
    diceTotal: number;
    firstCubeRoll: number;
    secondCubeRoll: number;
    zone: string;
  };
}

export interface FinalScore {
  moneyScore: number;
  techScore: number;
  productScore: number;
  motivationScore: number;
  timeScore: number;
  bonusScore: number;
  totalScore: number;
}

export interface ModifierResponse {
  id: number;

  name: string;
  type: string;

  purchaseCost: number;
  upkeepCost: number;

  stageAllowed: number;

  owned: boolean;
}

export interface PurchaseResponse {
  gameId: number;
  modifierId: number;
  remainingMoney: number;
  ownedModifiers: string[];
  quantity: number;
}

export interface CrisisResponse {
  description: string;
}

export const getSpheres = async () => {
  return (await axiosInstance.get("/api/game/spheres")).data;
};

export const getMissions = async (
  sphereId: number,
  page: number = 0,
  size: number = 3
) => {
  return (
    await axiosInstance.get<Mission[]>("/api/missions", {
      params: { sphereId, page, size },
    })
  ).data;
};

export const startGame = async (req: StartGame) => {
  return (await axiosInstance.post<GameState>("/api/game/start", req)).data;
};

export const evaluateDesicion = async (gameId: number, req: string) => {
  return (
    await axiosInstance.post<DecisionResponse>(
      `/api/game/${gameId}/evaluate-decision`,
      { decision: req }
    )
  ).data;
};

export const evaluatePresentation = async (gameId: number, req: string) => {
  return (
    await axiosInstance.post<DecisionResponse>(
      `/api/game/${gameId}/evaluate-presentation`,
      { decision: req }
    )
  ).data;
};

export const state = async (gameId: number) => {
  return (await axiosInstance.get<GameState>(`/api/game/${gameId}/state`)).data;
};

export const modifiers = async (gameId: number) => {
  return (
    await axiosInstance.get<ModifierResponse[]>(`/api/game/${gameId}/modifiers`)
  ).data;
};

export const purchaseModifier = async (gameId: number, modifierId: number) => {
  return (
    await axiosInstance.post<PurchaseResponse>(
      `/api/game/${gameId}/modifiers`,
      { modifierId }
    )
  ).data;
};

export const generateCrisis = async (gameId: number) => {
  return (
    await axiosInstance.post<CrisisResponse>(
      `/api/game/${gameId}/generate-crisis`
    )
  ).data;
};
