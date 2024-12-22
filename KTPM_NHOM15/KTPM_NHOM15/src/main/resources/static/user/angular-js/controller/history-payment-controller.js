solar_app.controller('history_payment_controller', function ($scope, $window, $timeout, UserService, OrderService, OrderItermService, DiscountService) {

    $scope.activeTab = 'confirm';

    $scope.setActiveTab = function (tab) {
        $scope.activeTab = tab;
    };

    $scope.formatPrice = function (price) {
        return new Intl.NumberFormat('vi-VN', {currency: 'VND'}).format(price);
    };

    UserService.findUserBySession().then(function successCallback(response) {
        $scope.users = response.data;

        OrderService.findByUserId($scope.users.id).then(function successCallback(response) {
            $scope.history_order = response.data;

            $timeout(function () {
                $('#table-confirm').DataTable({
                    "order": [],
                    "paging": true,
                    "ordering": true,
                    "info": true,
                    "language": {
                        "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/vi.json"
                    }
                });
                $('#table-deliver').DataTable({
                    "order": [],
                    "paging": true,
                    "ordering": true,
                    "info": true,
                    "language": {
                        "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/vi.json"
                    }
                });
                $('#table-delivered').DataTable({
                    "order": [],
                    "paging": true,
                    "ordering": true,
                    "info": true,
                    "language": {
                        "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/vi.json"
                    }
                });
                $('#table-cancelled').DataTable({
                    "order": [],
                    "paging": true,
                    "ordering": true,
                    "info": true,
                    "language": {
                        "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/vi.json"
                    }
                });
            });
        });
    });

    $scope.getInfoOrder = function (orderId) {
        let combinedData = [];
        let totalAmount = 0;
        let shippingFee = 0;
        let subTotal = 0;
        let discountId;

        OrderService.findByOrderId(orderId).then(function successCallback(response) {
            discountId = response.data.discountId;
            $scope.order = response.data;

            $scope.getStatusText = function () {
                let paymentStatus = $scope.order.paymentStatus
                switch (paymentStatus) {
                    case 0:
                        return 'Chưa thanh toán';
                    case 1:
                        return 'Đã thanh toán';
                    case 2:
                        return 'Thanh toán thất bại';
                    default:
                        return 'Trạng thái không xác định';
                }
            };

            if (discountId !== null) {
                DiscountService.findDiscountByDiscountId(discountId).then(function successCallback(response) {
                    $scope.discountCost = response.data.discountCost;
                });
            } else {
                $scope.discountCost = 0;
            }

            OrderItermService.findByOrderId($scope.order.id).then(function successCallback(response) {
                let listOrderItemAndProduct = $scope.order_items = response.data;

                for (let i = 0; i < listOrderItemAndProduct.length; i++) {
                    let data = {
                        product: listOrderItemAndProduct[i][1], orderItem: listOrderItemAndProduct[i][0]
                    };
                    combinedData.push(data);

                    // tạm tính
                    subTotal += (data.orderItem.quantity * data.orderItem.price);
                    shippingFee = $scope.order.orderShipCost;
                    // Tính tổng tiền dựa trên số lượng và giá của orderItem
                    totalAmount = subTotal - $scope.discountCost + shippingFee;
                }

                $scope.combinedData = combinedData;
                $scope.subTotal = subTotal;
                $scope.shippingFee = shippingFee;
                $scope.totalAmount = totalAmount;
            });
        });

        // Watch for changes in discountId and update discountCost accordingly
        $scope.$watch('discountId', function (newDiscountId, oldDiscountId) {
            if (newDiscountId !== oldDiscountId) {
                if (newDiscountId !== null) {
                    DiscountService.findDiscountByDiscountId(newDiscountId).then(function successCallback(response) {
                        $scope.discountCost = response.data.discountCost;
                    });
                } else {
                    // If there's no discount, set discountCost to 0
                    $scope.discountCost = 0;
                }
            }
        });
    };

    $scope.selectedReason = '';
    $scope.additionalComments = '';
    $scope.showAdditionalComments = false;

    $scope.cancelOrder = function () {
        $('#modal-confirm-cancellation').modal('show');
        $('#modal-confirm-data').modal('hide');
    }

    $scope.checkOtherOption = function (selectedReason) {
        $scope.showAdditionalComments = selectedReason === "Mục khác...";

        if (!$scope.showAdditionalComments) {
            $scope.additionalComments = '';
        }
    };

    $scope.confirmCancelOrder = function (orderId, selectedReason, additionalComments) {
        if (selectedReason || additionalComments) {
            let data = {
                orderId: orderId,
                reason: selectedReason,
                comments: additionalComments
            };

            OrderService.cancelOrderById(data).then(function successCallback() {
                centerAlert('Thành công !', 'Đơn hàng ' + orderId + ' của bạn đã được huỷ thành công !', 'success');
                setTimeout(function () {
                    $window.location.reload();
                }, 1500);
                $scope.apply();
            })

            $('#modal-confirm-cancellation').modal('hide');
        }
    }
});