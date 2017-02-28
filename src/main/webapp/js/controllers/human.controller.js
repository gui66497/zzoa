'use strict';

//人事controller
indexModule.controller('humanController', function ($scope, $http, $timeout) {

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
                { "name" : "人事档案", "id" : "2", "isTop":false, "url": "page/user.html", "children" : [] },
                { "name" : "合同管理", "id" : "3", "isTop":false, "url": "page/excelTest.html", "children" : [] },
                { "name" : "转正管理", "id" : "4", "isTop":false, "children" : [] }
            ]},
            { "name" : "培训管理", "id" : "5", "isTop":true, "children" : [
                { "name" : "大数据培训", "id" : "6", "isTop":false, "children" : [] }
            ] }
        ];

    //初始化时左侧默认展开
    $scope.expandedNodes = [$scope.treeData[0]];

    $scope.contents = [$scope.treeData[0].children[0]];

    /**
     * 选中左侧分类
     * @param node
     */
    $scope.nodeSelect = function (node) {
        if (!isIn(node, $scope.contents)) {
            $scope.contents.push(node);
            bindCloseTabSpan();
        }

        console.log("当前contens内容" ,$scope.contents);
    };

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
                console.log($(this));
                $(this).on('click', function () {
                    $(this).closest("li").hide();
                    console.log("id值为： " ,$(this).attr("id"));
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