enum Tokens {
  accessToken = "accessToken",
  refreshToken = "refreshToken",
}

const TokenService = {
  getAccessToken: () => localStorage.getItem(Tokens.accessToken),
  setAccessToken: (token: string) => localStorage.setItem(Tokens.accessToken, token),
  getRefreshToken: () => localStorage.getItem(Tokens.refreshToken),
  setRefreshToken: (token: string) => localStorage.setItem(Tokens.refreshToken, token),
  removeTokens: () => {
    localStorage.removeItem(Tokens.accessToken);
    localStorage.removeItem(Tokens.refreshToken);
  }
};

export default TokenService;
