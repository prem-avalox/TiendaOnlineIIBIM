<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>

    <div>
        <h3>Mi Bolsa</h3>
        <p>Tienes <strong>3</strong> prendas</p>
        <hr>
        
        <ul>
            <li>
                <img src="img/sweater.jpg" alt="Military Knit Sweater" width="100">
                <div>
                    <strong>Military Knit Sweater - Black</strong>
                    <p>$345.00</p>
                    <p>Size: XS</p>
                    <div>
                        <button type="button">-</button>
                        <span>01</span>
                        <button type="button">+</button>
                        <button type="button">Eliminar</button>
                    </div>
                </div>
            </li>
            <br>

            <li>
                <img src="img/bag.jpg" alt="Scandicci Suede Bag" width="100">
                <div>
                    <strong>Scandicci Suede Slouch Bag - Dark Brown</strong>
                    <p>$530.00</p>
                    <p>Size: ONE SIZE</p>
                    <div>
                        <button type="button">-</button>
                        <span>01</span>
                        <button type="button">+</button>
                        <button type="button">Eliminar</button>
                    </div>
                </div>
            </li>
            <br>

            <li>
                <img src="img/shirt.jpg" alt="Marcello Shirt" width="100">
                <div>
                    <strong>Marcello Shirt V2 - Navy</strong>
                    <p>$370.00 x 2</p>
                    <p>Size: S</p>
                    <div>
                        <button type="button">-</button>
                        <span>02</span>
                        <button type="button">+</button>
                        <button type="button">Eliminar</button>
                    </div>
                </div>
            </li>
        </ul>

        <hr>
        <p><strong>TOTAL: $1,615.00</strong></p>
        
        <div>
            <button type="button">Vaciar Bolsa</button>
        </div>
    </div>

</body>
</html>