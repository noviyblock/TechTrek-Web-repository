import { Game } from "../../api/services/UserService";
import { Color, defaultBlockColor } from "../../shared/Color";


const GamesHistory: React.FC<{ games: Game[] }> = ({ games }) => (
  <div className="flex flex-col gap-3 h-screen text-white max-h-max">
    <div className="font-inter text-lg">История</div>
    <div className="overflow-y-auto" style={{maxHeight: "calc(100% - 200px)"}}>
      <table className="w-full">
        <thead className="sticky top-0" style={{background: defaultBlockColor}}>
          <tr>
            <th className="text-left p-2">Компания</th>
            <th className="text-left p-2">Сфера</th>
            <th className="text-left p-2">Балл</th>
            <th className="text-left p-2">Статус</th>
          </tr>
        </thead>
        <tbody>
          {games.map((game, idx) => (
            <tr key={idx} className="">
              <td className="p-2">{/* Компания */}</td>
              <td className="p-2">{game.sphere}</td>
              <td className="p-2">{game.finalScore ? `${game.finalScore}/100` : "?"}</td>
              <td className="p-2">{/* Статус */}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
);

export default GamesHistory;
