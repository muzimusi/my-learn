package me.arjenlee.flinklearn.java;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.JoinOperator;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 小米5	1998.00	1001	2018-03-10	15：30：11
 * 茅台王子酒	366.00	1001	2018-03-10	15：30：11
 * 法国进口拉菲	708.00	1002	2018-03-10	15：30：12
 * 东北大辣皮	15.50	1002	2018-03-10	15：30：12
 * <p>
 * 1001	张三丰	男	1990-10-11
 * 1002	张无忌	男	1992-10-10
 * 1003	小龙女	女	1993-10-13
 * <p>
 * 小米5	1998.00	张三丰	男	2018-03-10	15：30：11
 * 茅台王子酒	366.00	张三丰	男	2018-03-10	15：30：11
 * 法国进口拉菲	708.00	张无忌	男	2018-03-10	15：30：12
 * 东北大辣皮	15.50	张无忌	男	2018-03-10	15：30：12
 */

public class OrderUserJoin {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        orderUserJoinJava(env);
    }

    public static void orderUserJoinJava(ExecutionEnvironment env) throws Exception {
        DataSource<String> orderRawDataSource =
                env.readTextFile("D:\\bigDatas\\inputs\\join\\order.txt");
        DataSource<String> userRawDataSource =
                env.readTextFile("D:\\bigDatas\\inputs\\join\\user.txt");

        MapOperator<String, Order> orderMapOperator =
                orderRawDataSource.map(new MapFunction<String, Order>() {
            @Override
            public Order map(String input) throws Exception {
                String[] splits = input.split("\t", 4);
                Order order = Order.builder()
                        .productName(splits[0])
                        .price(Double.valueOf(splits[1]))
                        .userId(Long.valueOf(splits[2]))
                        .time(LocalDateTime.parse(splits[3],
                                DateTimeFormatter.ofPattern("yyyy-MM-dd\tHH：mm：ss")))
                        .build();
                return order;
            }
        });
        MapOperator<String, User> userMapOperator =
                userRawDataSource.map(new MapFunction<String, User>() {
            @Override
            public User map(String input) throws Exception {
                String[] split = input.split("\t");
                User user = User.builder()
                        .id(Long.valueOf(split[0]))
                        .name(split[1])
                        .sex(split[2])
                        .birthday(LocalDate.parse(split[3],
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .build();
                return user;
            }
        });

        JoinOperator.DefaultJoin<Order, User> orderUserJoin =
                orderMapOperator.join(userMapOperator)
                .where("userId")
                .equalTo("id");

        MapOperator<Tuple2<Order, User>, OrderJoinUser> joinUserMapOperator =
                orderUserJoin.map(new MapFunction<Tuple2<Order, User>, OrderJoinUser>() {
            @Override
            public OrderJoinUser map(Tuple2<Order, User> tuple) throws Exception {
                OrderJoinUser orderJoinUser = OrderJoinUser.builder()
                        .productName(tuple.f0.getProductName())
                        .price(tuple.f0.getPrice())
                        .customName(tuple.f1.getName())
                        .sex(tuple.f1.getSex())
                        .time(tuple.f0.getTime())
                        .build();
                return orderJoinUser;
            }
        });

        for (OrderJoinUser orderJoinUser : joinUserMapOperator.collect()) {
            System.out.println(orderJoinUser.getProductName() + "\t"
                    + orderJoinUser.getPrice() + "\t"
                    + orderJoinUser.getCustomName() + "\t"
                    + orderJoinUser.getTime());
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Order {
        //小米5	1998.00	1001	2018-03-10	15：30：11
        private String productName;
        private Double price;
        private Long userId;
        @JsonFormat(pattern = "yyyy-MM-dd\tHH：mm：ss")
        private LocalDateTime time;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class User {
        //1001	张三丰	男	1990-10-11
        private Long id;
        private String name;
        private String sex;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthday;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class OrderJoinUser {
        // 小米5	1998.00	张三丰	男	2018-03-10	15：30：11
        private String productName;
        private Double price;
        private String customName;
        private String sex;
        @JsonFormat(pattern = "yyyy-MM-dd\tHH：mm：ss")
        private LocalDateTime time;
    }
}
