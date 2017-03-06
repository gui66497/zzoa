'use strict';

//人事controller
indexModule.controller('humanController', function ($scope, $http, $timeout) {

    $scope.contents = [];

    //左侧树的option
    $scope.treeOptions = {
        nodeChildren: "children",
        dirSelectable: false,
        isSelectable: function(node) {
            return !node.isTop;
        },
        injectClasses: {
            "ul": "c-ul",
            "li": "c-li",
            "liSelected": "c-liSelected",
            "iExpanded": "c-iExpanded",
            "iCollapsed": "c-iCollapsed",
            "iLeaf": "c-iLeaf",
            "label": "c-label",
            "labelSelected": "c-labelSelected"
        }
    };

    $scope.treeData =
        [
            { "name" : "人事管理", "id" : "1", "isTop":true, "children" : [
                { "name" : "员工档案", "id" : "2", "isTop":false, "url": "page/staff.html", "children" : [] },
                { "name" : "用户信息", "id" : "3", "isTop":false, "url": "page/user.html", "children" : [] },
                { "name" : "合同管理", "id" : "4", "isTop":false, "url": "page/excelTest.html", "children" : [] },
                { "name" : "转正管理", "id" : "5", "isTop":false, "url": "page/tableTest.html", "children" : [] }
            ]},
            { "name" : "培训管理", "id" : "6", "isTop":true, "children" : [
                { "name" : "大数据培训", "id" : "7", "isTop":false, "url": "page/formTest.html", "children" : [] }
            ] }
        ];

    //初始化时左侧默认展开
    $scope.expandedNodes = [$scope.treeData[0]];

    //$scope.contents = [$scope.treeData[0].children[0]];

    $scope.activeIndex = $scope.treeData[0].children[0].id;

    /**
     * 选中左侧分类
     * @param node
     */
    $scope.nodeSelect = function (node) {
        if (!isIn(node, $scope.contents)) {
            $scope.contents.push(node);
            bindCloseTabSpan();
        }
        //激活新加入的tab页,这里必须设置延迟，否则无效，不知为何
        $timeout(function () {
            $scope.activeIndex = node.id;
        }, 50);
        //console.log("当前contens内容" ,$scope.contents);
    };

    //默认选中第一个分类
    if ($scope.treeData != null) {
        if ($scope.treeData[0].children != null) {
            $scope.nodeSelect($scope.treeData[0].children[0]);
        }
    }

    //关闭某个tab
    function closeTab(nodeName) {
        var order = -1;
        angular.forEach($scope.contents, function (data, index, array) {
            if (nodeName == data.name) {
                order = index;
            }
        });
        $scope.contents.splice(order, 1);
    }

    //判断all是否包含one
    function isIn(one, all) {
        var isIn = false;
        angular.forEach(all, function (data, index, array) {
            if (data == one) {
                isIn = true;
            }
        });
        return isIn;
    }

    function bindCloseTabSpan() {
        $timeout(function () {
            $("span[name='closeTabSpan']").each(function () {
                $(this).on('click', function () {
                    $(this).closest("li").hide();
                    closeTab($(this).attr("id"));
                });
            });
        }, 300);
    }

    $(function () {
        //标签关闭按钮初始化
        bindCloseTabSpan();
    });

});