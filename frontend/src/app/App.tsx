import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import RegistrationPage from "../atomic/page/RegistrationPage";
import SphereSelectionPage from "../atomic/page/SphereSelectionPage";
import MissionPage from "../atomic/page/MissionPage";
import CommandNamePage from "../atomic/page/CommandNamePage";
import MainScreenLayout from "../atomic/template/MainScreenLayout";
import TestPage from "../atomic/page/TestPage";
import LoginPage from "../atomic/page/LoginPage";
import ProtectedRoute from "./ProtectedRoute";
import GamePage from "../atomic/page/GamePage";
import ProfilePage from "../atomic/page/ProfilePage";
import { nullGameState } from "../shared/constants";
import Cube from "../atomic/atom/Cube";
import DecisionField from "../atomic/atom/DecisionField";
import { useState } from "react";
import HeadedBlock from "../atomic/atom/HeadedBlock";
import DiceRoller from "../atomic/organism/DiceRoller";
import DiceResult from "../atomic/organism/DiceResult";
import Overlay from "../atomic/molecule/Overlay";
import BuyModifier from "../atomic/molecule/BuyCard";
import SectionBuyCard, {sectionType} from "../atomic/molecule/SectionBuyCard";
import BuyCardCarousel from "../atomic/organism/BuyCardCarousel";
import MarketSelection from "../atomic/molecule/MarketSelection";
import Market from "../atomic/organism/Market";
import CrisisDecision from "../atomic/molecule/CrisisDecision";

function App() {
  const [a, b] = useState<string>("");
  return (
    <Router>
      <Routes>
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/select"
          element={
            <ProtectedRoute>
              <SphereSelectionPage onClick={() => {}} />
            </ProtectedRoute>
          }
        />
        <Route
          path="/mission"
          element={
            <ProtectedRoute>
              <MissionPage missions={[]} onClick={() => {}} />
            </ProtectedRoute>
          }
        />
        <Route
          path="/command_name"
          element={
            <ProtectedRoute>
              <CommandNamePage onClick={() => {}} />
            </ProtectedRoute>
          }
        />
        <Route
          path="/main"
          element={
              <MainScreenLayout game={nullGameState}>da</MainScreenLayout>
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
