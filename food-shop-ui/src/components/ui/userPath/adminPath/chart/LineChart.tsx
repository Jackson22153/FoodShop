import { Line } from "react-chartjs-2";
import { Revenue } from "../../../../../model/Type";

type Prop = {
    revenue: Revenue[]
}
export default function MonthlyRevenueLineChart(prop: Prop){
    function getMonthName(monthNumber: number): string {
        const months = [
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        ];
        return months[monthNumber - 1] || "Invalid month";
    }


    return(
        <div className="chart-area">
            <Line
                data={{
                    labels: prop.revenue.map((data) => getMonthName(data.month)),
                    datasets: [
                        {
                            label: "Revenue (USD)",
                            data: prop.revenue.map((data) => data.total),
                            backgroundColor: "#064FF0",
                            borderColor: "#064FF0",
                        }
                    ],
                }}
                options={{
                    elements: {
                        line: {
                            tension: 0.5,
                        },
                    },
                    plugins: {
                        title: {
                            text: "Monthly Revenue",
                        },
                    },
                }}
            />
        </div>
    )
}
