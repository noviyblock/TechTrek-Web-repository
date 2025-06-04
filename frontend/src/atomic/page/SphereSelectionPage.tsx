import { useEffect, useState } from "react";
import { Sphere, getSpheres } from "../../api/Game";
import { categories } from "../../shared/constants";
import CardButtonTable from "../molecule/CardButtonTable";
import SingleElementLayoutTemplate from "../template/SingleElementLayout";
import { CardButtonProps } from "../../shared/Types";

const SphereSelectionPage: React.FC<{ onClick: (sphere: number) => void; onBack: () => void }> = ({
  onClick,
  onBack
}) => {
  const [cards, setCards] = useState<CardButtonProps[]>([]); 
  const [spheres, setSpheres] = useState<Sphere[]>([]);
  useEffect(() => {
    const loadSpheres = async () => {
      try {
        const response = await getSpheres();
        setSpheres(response);
      } catch (error) {
        console.error(error);
      }
    };
  
    loadSpheres();
  }, []);


  useEffect(() => {
    spheres?.forEach((value) => {
      const category = categories.find(
        (category) => category.name === value.name
      );
      if (category)
        category.id = value.id;
    });
    setCards(categories.filter((value) => value.id !== undefined));
  }, [spheres]);

  return (
    <SingleElementLayoutTemplate backable={true} onBack={onBack} width="66%" minWidth="950px">
      <div className="flex flex-col px-20 p-8 gap-4 ">
        <div className="text-white font-inter text-center">
          Выберите сферу в которой вы бы хотели запустить стартап:
        </div>
        <CardButtonTable categories={cards} onClick={onClick} />
      </div>
    </SingleElementLayoutTemplate>
  );
};

export default SphereSelectionPage;
