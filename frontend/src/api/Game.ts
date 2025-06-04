import axiosInstance, { safeRequest } from "./AxiosInstance";

export interface Sphere {
  id: number;
  name: string;
}

export interface GeneratedMission {
  first: string;
  second: string;
  third: string;
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

export const getSpheres = async () =>
  safeRequest<Sphere[]>(() => axiosInstance.get("/game/spheres"));

export const getMissions = async (
  sphereId: number,
  page: number = 0,
  size: number = 3
) =>
  safeRequest<Mission[]>(() =>
    axiosInstance.get<Mission[]>("/missions", {
      params: { sphereId, page, size },
    })
  );

export const generateMissions = async (sphereId: number) =>
  safeRequest<GeneratedMission>(() =>
    axiosInstance.post<GeneratedMission>("/generate-mission", {
      params: { sphereId },
    })
  );

export const startGame = async (req: StartGame) =>
  safeRequest<GameState>(() =>
    axiosInstance.post<GameState>("/game/start", req)
  );

export const evaluateDesicion = async (gameId: number, req: string) =>
  safeRequest<DecisionResponse>(() =>
    axiosInstance.post<DecisionResponse>(`/game/${gameId}/evaluate-decision`, {
      decision: req,
    })
  );

export const evaluatePresentation = async (gameId: number, req: string) =>
  safeRequest<DecisionResponse>(() =>
    axiosInstance.post<DecisionResponse>(
      `/game/${gameId}/evaluate-presentation`,
      { decision: req }
    )
  );

export const state = async (gameId: number) =>
  safeRequest<GameState>(() =>
    axiosInstance.get<GameState>(`/game/${gameId}/state`)
  );

export const modifiers = async (gameId: number) =>
  safeRequest<ModifierResponse[]>(() =>
    axiosInstance.get<ModifierResponse[]>(`/game/${gameId}/modifiers`)
  );

export const purchaseModifier = async (gameId: number, modifierId: number) =>
  safeRequest<PurchaseResponse>(() =>
    axiosInstance.post<PurchaseResponse>(`/game/${gameId}/modifiers`, {
      modifierId,
    })
  );

export const generateCrisis = async (gameId: number) =>
  safeRequest<CrisisResponse>(() =>
    axiosInstance.post<CrisisResponse>(`/game/${gameId}/generate-crisis`)
  );
