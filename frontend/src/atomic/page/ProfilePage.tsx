import { useEffect, useState } from "react";
import { User } from "../../api/services/UserService";
import { getUser } from "../../api/User";
import MediumButton from "../atom/MediumButton";
import { useNavigate } from "react-router-dom";
import { Color, deafultBackground } from "../../shared/Color";
import TitledText from "../atom/TitledText";
import ProfileBlock from "../organism/ProfileBlock";
import Block from "../molecule/Block";
import ProfileGame from "../organism/ProfileGame";
import GamesHistory from "./GamesHistory";

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
  };

  useEffect(() => {
    getUserInfo();
  }, []);

  return (
    <div
      className="flex flex-row gap-1 h-screen w-screen overflow-clip"
      style={{ background: deafultBackground }}
    >
      <ProfileBlock username={user.username} email={user.email} />
      <Block grow={1} h='h-screen'>
        <div className="flex flex-col gap-6 h-screen">
          <ProfileGame></ProfileGame>
          <div>
            <hr className="-mx-4" style={{ borderColor: Color["Gray"] }} />
          </div>
            <GamesHistory games={user.games}></GamesHistory>
        </div>
      </Block>
    </div>
  );
};

export default ProfilePage;
