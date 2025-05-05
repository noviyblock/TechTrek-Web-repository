import { useNavigate } from "react-router-dom";
import { status } from "../api/Auth";
import React, { useEffect, useState } from "react";
import { deafultBackground } from "../shared/Color";

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

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

  if (!isAuthenticated && false) {
    navigate("/login");
  }

  return <div>{children}</div>;
}

export default ProtectedRoute;
