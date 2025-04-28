import { categories } from "../../shared/constants";
import CardButtonTable from "../molecule/CardButtonTable";
import SingleElementLayoutTemplate from "../template/SingleElementLayout";

const SphereSelectionPage: React.FC = () => (
  <SingleElementLayoutTemplate backable={true} width="66%" minWidth="950px">
    <div className="flex flex-col px-20 p-8 gap-4 ">
      <div className="text-white font-inter text-center">
        Выберите сферу в которой вы бы хотели запустить стартап:
      </div>
      <CardButtonTable categories={categories} />
    </div>
  </SingleElementLayoutTemplate>
);

export default SphereSelectionPage;
