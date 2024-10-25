import { Bar } from "react-chartjs-2";
import { Revenue } from "../../../../../model/Type";

type Prop ={
    revenue: Revenue[]
}
export default function PaymentPercentageBarChar(prop: Prop){
    function getMonthName(monthNumber: number): string {
        const months = [
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        ];
        return months[monthNumber - 1] || "Invalid month";
    }
    
    return(
        <div className="chart-bar">
            <Bar
                data={{
                    labels: prop.revenue.map((data) => getMonthName(data.month)),
                    datasets: [
                        {
                            label: "Revenue (USD)",
                            data: prop.revenue.map((data) => data.total),
                            backgroundColor: [
                                "rgba(43, 63, 229, 0.8)",
                            ],
                            borderRadius: 5,
                        }
                    ],
                }}
                options={{
                    plugins: {
                        title: {
                            text: "Revenue",
                        },
                    },
                }}
            />
        </div>
    )
}