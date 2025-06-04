import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import RegistrationPage from "../atomic/page/RegistrationPage";
import MainScreenLayout from "../atomic/template/MainScreenLayout";
import LoginPage from "../atomic/page/LoginPage";
import ProtectedRoute from "./ProtectedRoute";
import GamePage from "../atomic/page/GamePage";
import ProfilePage from "../atomic/page/ProfilePage";
import { nullGameState } from "../shared/constants";
import Cube from "../atomic/atom/Cube";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/main"
          element={
              <MainScreenLayout game={nullGameState} sphere={0} gameId={0}>da</MainScreenLayout>
          }
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <ProfilePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/game"
          element={
            <ProtectedRoute>
              <GamePage />
            </ProtectedRoute>
          }
        />
        <Route path="/cube"
        element={
          <Cube amount={3} rotation={0}></Cube>
        }/>

        <Route path="/test" element={<div className="bg-black h-screen"><Cube amount={3} rotation={100}></Cube></div>} />
      </Routes>
    </Router>
  );
}

export default App;
