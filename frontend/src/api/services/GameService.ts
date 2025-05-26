export enum GameFields {
  gameId = "gameId",
  sphere = "sphere",
  mission = "mission",
  commandName = "commandName",
  gamePage = "gamePage",
  currentStage = "currentStage",
  crisisDecision = "crisisDecision",
  decision = "decision",
  gameState = "gameState",
}

export const GameService = {
  setGameId: (gameId: number) => {
    localStorage.setItem(GameFields.gameId, gameId.toString());
  },

  getGameId: (): number | undefined => {
    const gameId = localStorage.getItem(GameFields.gameId);
    return gameId === null ? undefined : parseInt(gameId);
  },

  removeGameId: () => {
    localStorage.removeItem(GameFields.gameId);
  },

  setSphere: (sphere: number) => {
    localStorage.setItem(GameFields.sphere, sphere.toString());
  },

  getSphere: (): number | undefined => {
    const sphere = localStorage.getItem(GameFields.sphere);
    return sphere === null ? undefined : parseInt(sphere);
  },

  removeSphere: () => {
    localStorage.removeItem(GameFields.sphere);
  },

  setMission: (mission: number) => {
    localStorage.setItem(GameFields.mission, mission.toString());
  },

  getMission: (): number | undefined => {
    const mission = localStorage.getItem(GameFields.mission);
    return mission === null ? undefined : parseInt(mission);
  },

  removeMission: () => {
    localStorage.removeItem(GameFields.mission);
  },

  setCommandName: (commandName: string) => {
    localStorage.setItem(GameFields.commandName, commandName);
  },

  getCommandName: (): string | undefined => {
    const commandName = localStorage.getItem(GameFields.commandName);
    return commandName ?? undefined;
  },

  removeCommandName: () => {
    localStorage.removeItem(GameFields.commandName);
  },

  setGamePage: (gamePage: string) => {
    localStorage.setItem(GameFields.gameId, gamePage);
  },

  getGamePage: (): string | undefined => {
    const gamePage = localStorage.getItem(GameFields.gamePage);
    return gamePage ?? undefined;
  },

  removeGamePage: () => {
    localStorage.removeItem(GameFields.gamePage);
  },

  setCurrentStage: (currentStage: string) => {
    localStorage.setItem(GameFields.currentStage, currentStage);
  },

  getCurrentStage: (): string | undefined => {
    const currentStage = localStorage.getItem(GameFields.currentStage);
    return currentStage ?? undefined;
  },

  removeCurrentStage: () => {
    localStorage.removeItem(GameFields.currentStage);
  },

  removeCrisisDecision: () => {
    localStorage.removeItem(GameFields.crisisDecision);
  },
  removeDecision: () => {
    localStorage.removeItem(GameFields.decision);
  },
  removeGameState: () => {
    localStorage.removeItem(GameFields.gameState);
  },

  reset: () => {
    GameService.removeGameId();
    GameService.removeGamePage();
    GameService.removeMission();
    GameService.removeCommandName();
    GameService.removeSphere();
    GameService.removeCurrentStage();
    GameService.removeCrisisDecision();
    GameService.removeDecision();
    GameService.removeGameState();
  },
};
