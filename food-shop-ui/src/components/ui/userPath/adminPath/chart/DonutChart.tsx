import { Doughnut } from "react-chartjs-2";
import { PaymentStatusPercentage } from "../../../../../model/Type";

type Prop = {
    paymentStatusPercentage: PaymentStatusPercentage[]
}
export default function MonthlyRevenueDonutChart(prop: Prop){
    function capitalizeFirstLetter(str: string): string {
        return str.charAt(0).toUpperCase() + str.slice(1);
    }

    return(
        <div className="chart-pie">
            <Doughnut
                data={{
                    labels: prop.paymentStatusPercentage.map((data) => capitalizeFirstLetter(data.status)),
                    datasets: [
                    {
                        label: "Count",
                        data: prop.paymentStatusPercentage.map((data) => data.percentage),
                        backgroundColor: [
                            "rgba(43, 63, 229, 0.8)",
                            "rgba(250, 192, 19, 0.8)",
                            "rgba(253, 135, 135, 0.8)",
                            "rgba(102, 184, 255, 0.8)",
                            "rgba(247, 134, 159, 0.8)",
                            "rgba(229, 103, 229, 0.8)",
                            "rgba(153, 153, 153, 0.8)",
                            "rgba(54, 162, 235, 0.8)",
                            "rgba(255, 220, 111, 0.8)"
                        ],
                        borderColor: [
                            "rgba(43, 63, 229, 0.8)",
                            "rgba(250, 192, 19, 0.8)",
                            "rgba(253, 135, 135, 0.8)",
                            "rgba(102, 184, 255, 0.8)",
                            "rgba(247, 134, 159, 0.8)",
                            "rgba(229, 103, 229, 0.8)",
                            "rgba(153, 153, 153, 0.8)",
                            "rgba(54, 162, 235, 0.8)",
                            "rgba(255, 220, 111, 0.8)"
                        ],
                    },
                    ],
                }}
                options={{
                    plugins: {
                        title: {
                            text: "Payment status",
                        },
                    },
                }}
            />
        </div>
    )
}