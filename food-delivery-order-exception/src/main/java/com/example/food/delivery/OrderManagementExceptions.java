package com.example.food.delivery;

public class OrderManagementExceptions {
    public static class BaseOrderManagementException extends RuntimeException {
        private boolean isSuccess;
        private String status;

        public BaseOrderManagementException(String message) {
            super(message);
            this.isSuccess = false;
            this.status = "Error";
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class InvalidInputException extends BaseOrderManagementException {
        public InvalidInputException(String message) {
            super(message);
        }
    }


    public static class UnrecognizedTokenException extends RuntimeException {
        public UnrecognizedTokenException(String message) {
            super(message);
        }
    }

    public static class RestaurantNotFound extends BaseOrderManagementException {
        public RestaurantNotFound(String message) {
            super(message);
        }
    }

    public static class CartNotFound extends BaseOrderManagementException {
        public CartNotFound(String message) {
            super(message);
        }
    }

    public static class OrderNotFound extends BaseOrderManagementException {
        public OrderNotFound(String message) {
            super(message);
        }
    }

    public static class OrderItemNotFound extends BaseOrderManagementException {
        public OrderItemNotFound(String message) {
            super(message);
        }
    }

    public static class DiffRestaurantIdException extends BaseOrderManagementException {
        public DiffRestaurantIdException(String message) {
            super(message);
        }
    }

    public static class DeliveryNotFound extends BaseOrderManagementException {
        public DeliveryNotFound(String message) {
            super(message);
        }
    }

    public static class DuplicateException extends BaseOrderManagementException {
        public DuplicateException(String message) {
            super(message);
        }
    }

    public static class RestTemplateException extends BaseOrderManagementException {
        public RestTemplateException(String message) {
            super(message);
        }
    }

    public static class UserAlreadyExistsException extends BaseOrderManagementException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }


}