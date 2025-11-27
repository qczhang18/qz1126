build
./gradlew clean build

run
java -jar ./build/libs/rental-0.0.1-SNAPSHOT.jar

POST localhost:8080/rent

with body
```
{
    "tool": "LADW",
    "rentalDay": 4,
    "discountPercent": 0,
    "checkoutDate": "07/02/22"
}
```

Response
```
{
    "agreement": {
        "toolCode": "LADW",
        "toolType": "Ladder",
        "toolBrand": "Werner",
        "rentalDays": 4,
        "checkoutDate": "07/02/22",
        "dueDate": "07/06/22",
        "dailyRentalCharge": 1.99,
        "chargeDays": 3,
        "preDiscountCharge": 5.97,
        "discountPercent": 0,
        "discountAmount": 0.00,
        "finalCharge": 5.97
    },
    "message": "success"
}
```


CONSOLE
```
Generated agreement:
Tool code: LADW
Tool type: Ladder
Tool brand: Werner
Rental days: 4
Checkout date: 07/02/22
Due date: 07/06/22
Daily rental charge: $1.99
Charge days: 3
Pre-discount charge: $5.97
Discount percent: 0%
Discount amount: $0.00
Final charge: $5.97
```