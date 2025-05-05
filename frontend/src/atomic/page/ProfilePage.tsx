import { useEffect, useState } from "react";
import { User } from "../../api/services/UserService";
import { getUser } from "../../api/User";
import MediumButton from "../atom/MediumButton";
import { useNavigate } from "react-router-dom";

const ProfilePage: React.FC = () => {
  const [user, setUser] = useState<User>({
    username: "",
    email: "",
    games: [],
  });
  const navigate = useNavigate();

  const getUserInfo = async () => {
    try {
      const response = await getUser();
      setUser(response);
    } catch (error) {
      console.error(error);
    }
  };

  const newGameOnClick = () => {
    navigate("/game");
  }

  useEffect(() => {
    getUserInfo();
  });

  return (
    <div>
      <div>
        username: {user.username}, email: {user.email}, games: {JSON.stringify(user.games)}
      </div>
      <MediumButton onClick={newGameOnClick}>New game</MediumButton>
    </div>
  );
};

export default ProfilePage;
