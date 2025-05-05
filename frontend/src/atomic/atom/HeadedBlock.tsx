const HeadedBlock: React.FC<{header: string; children: React.ReactNode}> = ({ header, children }) => (
    <div className="p-4 flex flex-col gap-3 border-solid border text-white" style={{background: "#18181B", borderColor: '#3C3C3C'}}>
        <b className="font-inter">{header}</b>
        <div className="font-inter text-sm">{children}</div>
    </div>
);
export default HeadedBlock;
