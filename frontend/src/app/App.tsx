import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import RegistrationPage from "../atomic/page/RegistrationPage";
import SphereSelectionPage from "../atomic/page/SphereSelectionPage";
import MissionPage from "../atomic/page/MissionPage";
import CommandNamePage from "../atomic/page/CommandNamePage";
import MainScreenLayout from "../atomic/template/MainScreenLayout";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/select" element={<SphereSelectionPage />} />
        <Route path="/mission" element={<MissionPage />} />
        <Route path="/command_name" element={<CommandNamePage />} />
        <Route path="/main" element={<MainScreenLayout />} />

        <Route path="/test" element={<MainScreenLayout/>}/>
      </Routes>
    </Router>
  );
}

export default App;
