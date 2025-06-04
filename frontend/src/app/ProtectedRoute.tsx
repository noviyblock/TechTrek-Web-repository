import { useLocation, useNavigate } from "react-router-dom";
import { status } from "../api/Auth";
import React, { useEffect, useState } from "react";
import { deafultBackground } from "../shared/Color";

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const location = useLocation()

  const checkAuthStatus = async () => {
    try {
      const response = await status();
      setIsAuthenticated(response);
    } catch (err) {
      setIsAuthenticated(false);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    checkAuthStatus();
  }, []);

  if (loading) {
    return <div style={{background: deafultBackground}}></div>;
  }

  if (!isAuthenticated && location.pathname.indexOf("game") == -1) {
    navigate("/login");
  }

  return <div>{children}</div>;
}

export default ProtectedRoute;
