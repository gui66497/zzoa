'use strict';


indexModule.directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs, ngModel) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;
                element.bind('change', function(event){
                    scope.$apply(function(){
                        modelSetter(scope, element[0].files[0]);
                    });
                    //附件预览
                    scope.file = (event.srcElement || event.target).files[0];
                    scope.getFile();
                });
            }
        };
    }]);

indexModule.factory('fileReader', ["$q", "$log", function($q, $log){
    var onLoad = function(reader, deferred, scope) {
        return function () {
            scope.$apply(function () {
                deferred.resolve(reader.result);
            });
        };
    };
    var onError = function (reader, deferred, scope) {
        return function () {
            scope.$apply(function () {
                deferred.reject(reader.result);
            });
        };
    };
    var getReader = function(deferred, scope) {
        var reader = new FileReader();
        reader.onload = onLoad(reader, deferred, scope);
        reader.onerror = onError(reader, deferred, scope);
        return reader;
    };
    var readAsDataURL = function (file, scope) {
        var deferred = $q.defer();
        var reader = getReader(deferred, scope);
        reader.readAsDataURL(file);
        return deferred.promise;
    };
    return {
        readAsDataUrl: readAsDataURL
    };
}]);

indexModule.controller('newStaffController',['$scope', '$http','$timeout', 'NgTableParams','fileReader', function ($scope, $http, $timeout, NgTableParams,fileReader) {

    $scope.allOver=[];
    $scope.allNum=[];
    if($scope.selectStaff.passport!==null){
        $scope.marLeft={
            'margin-left':'18%'
        };
        $scope.splitOut=$scope.selectStaff.passport.split(",");
        for(var i=0;i<$scope.splitOut.length;i++){
            $scope.allOver.push($scope.splitOut[i].substr(0,$scope.splitOut[i].indexOf("|")));
            $scope.allNum.push($scope.splitOut[i].substr($scope.splitOut[i].lastIndexOf("|") + 1))
        }
    }else{
        $scope.marLeft={
            'position':'relative',
            'left':'0',
            'top':'-15px'
        };
    }

    $scope.imageSrc = $scope.selectStaff.photo;

    // 图片
    $scope.getFile = function () {
        fileReader.readAsDataUrl($scope.file, $scope).then(function(result) {
                $scope.imageSrc = result;
            if($scope.imageSrc==null){
                $scope.imgHead=false;
            }else{
                $scope.imgHead=true;
            }
        });
    };



    // 名族
    $scope.chineseFamily=[
        {'id':1,'name':'汉族'},
        {'id':2,'name':'壮族 '},
        {'id':3,'name':'藏族'},
        {'id':4,'name':'裕固族'},
        {'id':5,'name':'彝族'},
        {'id':6,'name':'瑶族'},
        {'id':7,'name':'锡伯族'},
        {'id':8,'name':'乌孜别克族'},
        {'id':9,'name':'维吾尔族'},
        {'id':10,'name':'佤族'},
        {'id':11,'name':'土家族 '},
        {'id':12,'name':'土族 '},
        {'id':13,'name':'塔塔尔族'},
        {'id':14,'name':'塔吉克族'},
        {'id':15,'name':'水族'},
        {'id':16,'name':'畲族'},
        {'id':17,'name':'撒拉族'},
        {'id':18,'name':'羌族'},
        {'id':19,'name':'普米族'},
        {'id':20,'name':'怒族'},
        {'id':20,'name':'纳西族'},
        {'id':22,'name':'仫佬族'},
        {'id':23,'name':'苗族'},
        {'id':24,'name':'蒙古族'},
        {'id':25,'name':'门巴族'},
        {'id':26,'name':'毛南族'},
        {'id':27,'name':'满族'},
        {'id':28,'name':'珞巴族'},
        {'id':29,'name':'僳僳族'},
        {'id':30,'name':'黎族'},
        {'id':31,'name':'拉祜族'},
        {'id':32,'name':'柯尔克孜族'},
        {'id':33,'name':'景颇族'},
        {'id':34,'name':'京族 '},
        {'id':35,'name':'基诺族'},
        {'id':36,'name':'回族'},
        {'id':37,'name':'赫哲族 '},
        {'id':38,'name':'哈萨克族'},
        {'id':39,'name':'哈尼族'},
        {'id':40,'name':'仡佬族'},
        {'id':41,'name':'高山族'},
        {'id':42,'name':'鄂温克族 '},
        {'id':43,'name':'俄罗斯族'},
        {'id':44,'name':'鄂伦春族'},
        {'id':45,'name':'独龙族'},
        {'id':46,'name':'东乡族'},
        {'id':47,'name':'侗族 '},
        {'id':48,'name':'德昂族'},
        {'id':49,'name':'傣族 '},
        {'id':50,'name':'达斡尔族'},
        {'id':51,'name':'朝鲜族'},
        {'id':52,'name':'布依族'},
        {'id':53,'name':'布朗族'},
        {'id':54,'name':'保安族'},
        {'id':55,'name':'白族'},
        {'id':56,'name':'阿昌族'}
    ];

    // var postData = {
    //     vacationType: $scope.leave.type,
    //     reason: $scope.leave.reason,
    //     familyRelation: +$scope.leave.type == 7 ? $scope.leave.relation : "",
    //     startTime: startTime,
    //     endTime: endTime,
    //     fileName: $scope.imageSrc,
    //     workDelivers: workDelivers,
    //     ccmailNickNames: sendPersons,
    //     realDays: +$scope.leave.type == 8 ? $scope.leave.timeLong : ""
    // };
    // var promise = postMultipart('/maldives/leave/save', postData);
    // function postMultipart(url, data) {
    //     var fd = new FormData();
    //     angular.forEach(data, function(val, key) {
    //         fd.append(key, val);
    //     });
    //     var args = {
    //         method: 'POST',
    //         url: url,
    //         data: fd,
    //         headers: {'Content-Type': undefined},
    //         transformRequest: angular.identity
    //     };
    //     return $http(args);
    // }

    $scope.addBuild=false;
    //获取证件根据逗号排序
    $scope.addNew=function () {
        if(($scope.choiceCard==undefined || $scope.choiceType==undefined) && $scope.addBuild==true){
            alert("尚未输入完整！")
        }else if($scope.choiceCard!==undefined && $scope.choiceType!==undefined){


            $scope.allOver.push($scope.choiceType);
            $scope.allNum.push($scope.choiceCard);

            $timeout(function () {
                $scope.choiceCard=undefined;
                $scope.choiceType=undefined;
                $scope.addBuild=false;
                if($scope.choiceCard==undefined && $scope.choiceType==undefined && $scope.addBuild==false){
                    $scope.addBuild=true;
                    $scope.marLeft={
                        'margin-left':'18%'
                    };
                }
            })
        }else if($scope.choiceCard==undefined && $scope.choiceType==undefined && $scope.addBuild==false){
            $scope.addBuild=true;
        }

    };

    $scope.workYear=function () {
        $scope.selectStaff.seniority=parseFloat($scope.selectStaff.preSeniority)+parseFloat($scope.selectStaff.companyAge);
    };

    // 点击确认
    $scope.editorSure=function () {
        var passport='';
        for(var i=0;i<$scope.allOver.length;i++){
            passport+=$scope.allOver[i]+'|'+$scope.allNum[i]+',';
        }
        if($scope.choiceType==undefined || $scope.choiceCard==undefined){
            $scope.selectStaff.passport=passport.substring(0,passport.length-1)
        }else{
            $scope.selectStaff.passport=passport+$scope.choiceType+'|'+$scope.choiceCard;
        }

        if($scope.disPersonal==false){
            $scope.selectStaff.email=document.getElementById("before").value+document.getElementById("after").value;
            if($scope.runNumber!=null&&$scope.switchboard!=null){
                $scope.selectStaff.seat=document.getElementById("switchboard").value+'-'+document.getElementById("runNumber").value;
            }
        };
        var myPhone = document.getElementById('phone').value;
        var phone=myPhone.substr(0, 11);
        var ePhone = document.getElementById('emergencyPhone').value;
        var emergencyPhone=ePhone.substr(0, 11);
        var idCardNo = document.getElementById('idCardNo').value;
        if($scope.choiceCard!==undefined && $scope.choiceType!==undefined){
            $scope.allOver.push($scope.choiceType);
            $scope.allNum.push($scope.choiceCard);
            $timeout(function () {
                $scope.choiceCard=undefined;
                $scope.choiceType=undefined;
                $scope.addBuild=false;
            })
        }else{
            $scope.addBuild=false;
        }

        if(!(/^1[345789]\d{9}$/.test(phone))&&phone!==undefined&&phone!==""||
            !(/^1[345789]\d{9}$/.test(emergencyPhone))&&emergencyPhone!==undefined&&emergencyPhone!==""){
            alert("手机号码错误");
            return false;
        }else if(!(/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(idCardNo))&&idCardNo!==undefined&&idCardNo!==""){
            alert("身份证输入不合法");
            return false;
        }else{
            $scope.selectStaff.photo = $scope.imageSrc;
            $http.post("rest/staff/edit", $scope.selectStaff).success(function (data) {
                if (data != null && data != undefined) {
                    if (data.resultCode == "RESULT_SUCCESS") {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        }
    };


    //删除最后一个
    $scope.removeNew=function () {
        var indexOne=$scope.allOver.length-1;
        var indexNum=$scope.allNum.length-1;
        $scope.allOver.splice(indexOne, 1);
        $scope.allNum.splice(indexNum, 1);
    };


    $scope.req.userId = $scope.selectStaff.staffId;
    // 资质
    function certificate() {
        $http.post("rest/aptitude/"+$scope.req.userId+"/effectiveList",null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.effective=data.data;
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    }
    certificate();

    //失效资质
    $http.post("rest/aptitude/"+$scope.req.userId+"/invalidList",null).success(function (data) {
        if (data != null && data != undefined) {
            $scope.Invalid=data.data;
        }
    }).error(function (data) {
        angular.element("#serverErrorModal").modal({show: true});
    });
    // 归还/借出
    function returnLend() {
        $http.post("rest/aptitudeBorrow/list",$scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.returnLend=data.data;
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    }
    returnLend();


    // 合同
    function contracts() {
        $http.post("rest/cont/"+$scope.req.userId+"/effectiveList",null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.contract=data.data;
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    }
    contracts();

    // 失效合同
    $http.post("rest/cont/"+$scope.req.userId+"/invalidList",null).success(function (data) {
        if (data != null && data != undefined) {
            $scope.invalidContract=data.data;
        }
    }).error(function (data) {
        angular.element("#serverErrorModal").modal({show: true});
    });


    // 人员属性
    function attributes() {
        $http.post("rest/staffAttribute/list",$scope.req).success(function (data) {
            if (data != null && data != undefined) {
                $scope.attribute=data.data;
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });

        // 资质类型
        $http.get("rest/dic/STAFF_ATTRIBUTE/getDicInfo",null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.staffAttribute=data.data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
        // 人员属性
        $http.get("rest/dic/ATTRIBUTE_NAME/getDicInfo",null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.attributeSelect=data.data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    }
    attributes();

    // 部门的种类
    $http.get("rest/depart/allList",null).success(function (data) {
        if (data != null && data != undefined) {
            $scope.allList=data;
        }
    }).error(function (data) {
        $("#serverErrorModal").modal({show: true});
    });


    $scope.Establish=false;
    $scope.append=function () {
        if($scope.Establish==false){
            $scope.addSelect={};
            $scope.Establish=true;
        }else{
            if($scope.addSelect.attributeId!=undefined&&$scope.addSelect.attributeName!=undefined&&$scope.addSelect.departName!=undefined){
                $scope.addSelect.userId=$scope.selectStaff.staffId;
                $http.post("rest/staffAttribute/save", $scope.addSelect).success(function (data) {
                    if (data != null && data != undefined) {
                        if(data.resultCode=="RESULT_SUCCESS") {
                            attributes();
                            $scope.addSelect={};
                            $scope.Establish=true;
                        } else {
                            showOkModal('提示', data.message);
                        }
                    }
                }).error(function (data) {
                    $("#addAndUpdateProviderModal").modal({show: true});
                });
            } else if($scope.addSelect.attributeId==undefined || $scope.addSelect.attributeName==undefined || $scope.addSelect.departName==undefined){
                alert("请先填写完整上一条！")
            }
        }

    };
    $scope.btnDel=function () {
        $scope.Establish=false;
    };

    $scope.alter=function (index) {
        $scope.attribute[index].conceal=true;
    };

    $scope.changeStyle=true;
    $scope.disSelect=true;
    $scope.btnHide=false;
    // 人员属性编辑
    $scope.personalStyle=function () {
        $scope.changeStyle=!$scope.changeStyle;
        $scope.disSelect=!$scope.disSelect;
        $scope.btnHide=!$scope.btnHide;
        $scope.Establish=false;
        attributes();
    };
    // 人员属性删除
    $scope.deleteAttribute=function (index) {
        var Id = $scope.attribute[index].id;
        showConfirmModal("确认要删除吗？", function () {
            $http.delete("rest/staffAttribute/" + Id + "/delete").success(function (response) {
                showOkModal('提示', response.message);
                if (response.resultCode == "RESULT_SUCCESS") {
                    attributes();
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        });
    };
    // 人员属性保存
    $scope.addSelect={};
    $scope.personalSure=function () {
        $scope.addSelect.userId=$scope.selectStaff.staffId;
        if($scope.addSelect.attributeId==undefined ||$scope.addSelect.attributeName==undefined||$scope.addSelect.departName==undefined){
            alert("请填写完整！")
        }else{
            $http.post("rest/staffAttribute/save", $scope.addSelect).success(function (data) {
                if (data != null && data != undefined) {
                    if(data.resultCode=="RESULT_SUCCESS") {
                        attributes();
                        $scope.Establish=false;
                        $("#addContractModal").modal('hide');
                        showOkModal('提示', "成功！");
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                $("#addAndUpdateProviderModal").modal({show: true});
            });
        }

    };
    $scope.save=function (index) {
        $scope.addSelect.userId=$scope.selectStaff.staffId;
        $scope.addSelect=$scope.attribute[index];
        $http.post("rest/staffAttribute/save", $scope.addSelect).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    attributes();
                    $("#addContractModal").modal('hide');
                    showOkModal('提示', "成功！");
                } else {
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#addAndUpdateProviderModal").modal({show: true});
        });
    };

    // 合同修改
    $scope.modifyContract=function (index) {
        $scope.formData = $scope.contract[index];
        $scope.formData.isConNoEnd = $scope.formData.validity === "无固定期限" ? "0" : "1";
        angular.element("#addContractModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    // 合同上传
    $scope.showUploadCont=function (index) {
        $scope.uploadId =$scope.contract[index].id;
        angular.element("#uploadContModal").modal({show: true, keyboard: false, backdrop: 'static'});
        document.getElementById("contFileName").onchange = function() {
            var fileUser=document.getElementById("contFileName").value;
            $scope.formData.contFileName= fileUser.substr(fileUser.lastIndexOf("\\")+1);
            document.getElementById("contFile").value=$scope.formData.contFileName;
        };
    };
    // 合同新增
    $scope.openContract=function () {
        $scope.formData = {};
        //合同默认有固定期限
        $scope.formData.isConNoEnd = "1";
        angular.element("#addContractModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    $scope.addContract=function () {
        $scope.formData.userId=$scope.selectStaff.staffId;
        $scope.formData.userName=$scope.selectStaff.name;
        $http.post("rest/cont/save", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    contracts();
                    $("#addContractModal").modal('hide');
                    showOkModal('提示', "成功！");
                } else {
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#addAndUpdateProviderModal").modal({show: true});
        });
    };
    // 合同删除
    $scope.deleteContract=function (index) {
        var Id = $scope.contract[index].id;
        showConfirmModal("确认要删除吗？", function () {
            $http.delete("rest/cont/" + Id + "/delete").success(function (response) {
                showOkModal('提示', response.message);
                if (response.resultCode == "RESULT_SUCCESS") {
                    contracts();
                    $("#confirmModal").modal('hide');
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        });
    };

    // 合同固定期限切换
    $scope.conEndRadioChecked = function () {
        if ($scope.formData.isConNoEnd === "0") {
            $scope.formData.validity = "无固定期限";
            $scope.formData.endDate = "";
        } else {
            $scope.formData.validity="";
        }
    };

    // 合同上传
    $scope.contUpload = function () {
        var files = document.getElementsByName('contFileName')[0].files;
        if (files.length < 1) {
            showOkModal("提示信息", "请先选择文件！");
            return;
        }
        var fileTyp = files[0].name.substr(files[0].name.lastIndexOf("."));
        $http({
            method: 'POST',
            url: 'rest/cont/'+$scope.uploadId+'/'+fileTyp+'/upload',
            headers: {
                'Content-Type': undefined
            },
            data: {
                file: files[0]
            },
            transformRequest: function (data) {
                var formData = new FormData();
                formData.append('file', data.file);
                return formData;
            }
        }).success(function (data) {
            //请求成功
            showOkModal('提示', "添加成功！");
            self.tableParams.reload();
            angular.element("#uploadContModal").modal('hide');
        }).error(function (err, status) {
            console.log(err);
        });
    };

    /*############################################################################*/

    // 资质修改
    $scope.modifyCertificate=function (index) {
        $scope.formData = $scope.effective[index];
        $scope.formData.isCerNoEnd = $scope.formData.validity === "无固定期限" ? "0" : "1";
        angular.element("#addCertificateModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    // 证书上传
    $scope.showUploadAptitude=function (index) {
        $scope.uploadId2 =$scope.effective[index].id;
        angular.element("#uploadAptitudeModal").modal({show: true, keyboard: false, backdrop: 'static'});
        document.getElementById("aptitudeFileName").onchange = function() {
            var fileUser=document.getElementById("aptitudeFileName").value;
            $scope.formData.aptitudeFileName= fileUser.substr(fileUser.lastIndexOf("\\")+1);
            document.getElementById("aptitudeFile").value=$scope.formData.aptitudeFileName;
        };
    };
    // 资质新增
    $scope.openCertificate=function () {
        $scope.formData = {};
        $scope.formData.isCerNoEnd = "1";
        angular.element("#addCertificateModal").modal({show: true, keyboard: false, backdrop: 'static'});
        $http.get("rest/dic/APTITUDE_TYPE/getDicInfo",null).success(function (data) {
            if (data != null && data != undefined) {
                $scope.typeQualification=data.data;
            }
        }).error(function (data) {
            $("#serverErrorModal").modal({show: true});
        });
    };
    $scope.addCertificate=function () {
        $scope.formData.userId=$scope.selectStaff.staffId;
        $scope.formData.userName=$scope.selectStaff.name;
        $http.post("rest/aptitude/save", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    certificate();
                    $("#addCertificateModal").modal('hide');
                    showOkModal('提示', "成功！");
                } else {
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#addAndUpdateProviderModal").modal({show: true});
        });

    };
    // 资质删除
    $scope.deleteCertificate=function (index) {
        var Id = $scope.effective[index].id;
        showConfirmModal("确认要删除吗？", function () {
            $http.delete("rest/aptitude/" + Id + "/delete").success(function (response) {
                showOkModal('提示', response.message);
                if (response.resultCode == "RESULT_SUCCESS") {
                    certificate();
                    $("#confirmModal").modal('hide');
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        });
    };

    $scope.aptitudeUpload = function () {
        var files = document.getElementsByName('aptitudeFileName')[0].files;
        if (files.length < 1) {
            showOkModal("提示信息", "请先选择文件！");
            return;
        }
        var fileTyp = files[0].name.substr(files[0].name.lastIndexOf("."));
        $http({
            method: 'POST',
            url: 'rest/aptitude/'+$scope.uploadId2+'/'+fileTyp+'/upload',
            headers: {
                'Content-Type': undefined
            },
            data: {
                file: files[0]
            },
            transformRequest: function (data) {
                var formData = new FormData();
                formData.append('file', data.file);
                return formData;
            }
        }).success(function (data) {
            //请求成功
            showOkModal('提示', "添加成功！");
            self.tableParams.reload();
            angular.element("#uploadAptitudeModal").modal('hide');
        }).error(function (err, status) {
            console.log(err);
        });
    };
    // 借出/归还新增
    $scope.openReturn=function () {
        $scope.formData = {};
        angular.element("#returnLendModal").modal({show: true, keyboard: false, backdrop: 'static'});
    };
    $scope.addReturnLend=function () {
        $scope.formData.userId=$scope.selectStaff.staffId;
        $http.post("rest/aptitudeBorrow/save", $scope.formData).success(function (data) {
            if (data != null && data != undefined) {
                if(data.resultCode=="RESULT_SUCCESS") {
                    returnLend();
                    $("#returnLendModal").modal('hide');
                    showOkModal('提示', "成功！");
                } else {
                    showOkModal('提示', data.message);
                }
            }
        }).error(function (data) {
            $("#addAndUpdateProviderModal").modal({show: true});
        });
    };
    // 借出/归还删除
    $scope.deleteReturn=function (index) {
        var Id = $scope.returnLend[index].id;
        showConfirmModal("确认要删除吗？", function () {
            $http.delete("rest/aptitudeBorrow/" + Id + "/delete").success(function (response) {
                showOkModal('提示', response.message);
                if (response.resultCode == "RESULT_SUCCESS") {
                    returnLend();
                    $("#confirmModal").modal('hide');
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        });
    };

    // 资质固定期限切换
    $scope.cerEndRadioChecked = function () {
        if ($scope.formData.isCerNoEnd === "0") {
            $scope.formData.validity = "无固定期限";
            $scope.formData.endDate = "";
        } else {
            $scope.formData.validity="";
        }
    };

    $scope.beforeTime = function () {
        $scope.formatterTime();
        if ($scope.formData.isCerNoEnd === "0" || $scope.formData.isConNoEnd === "0") {
            //无固定期限不需要校验日期
            return;
        }
        if($scope.formData.isCerNoEnd !== "0" && $scope.formData.beginDate > $scope.formData.endDate){
            $scope.formData.endDate='';
            $scope.formData.validity='';
            alert('截止日期不能小于起始日期！')
        }else{
            $scope.formData.validity=DateDiff($scope.formData.beginDate,$scope.formData.endDate)+1;
        }

    };


    // 计算天数
    function  DateDiff(sDate1,  sDate2){
        var  aDate,  oDate1,  oDate2,  iDays;
        aDate  =  sDate1.split("-");
        oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]);
        aDate  =  sDate2.split("-");
        oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]);
        iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24);
        return  iDays;
    }



    var self = this;

    //当前员工id
    $scope.req.staffId = $scope.selectStaff.staffId;


    /************************************异动信息管理   开始**************************************/
    $scope.showAction = false;
    $scope.typeArr = ["入职", "转正", "岗位变动", "离职"];
    $scope.regularArr = ["正常转正","延长转正期1个月","提前转正期1个月"];

    //初始化自己的分页参数
    $scope.turnoverReq = angular.copy($scope.req);
    $scope.turnoverReq.paging = {"pageNo":1,"pageSize":10};

    self.tableParams = new NgTableParams({'count': 10}, {
        getData: function (params) {
            var promise = $http.post("rest/turnover/page", $scope.turnoverReq);
            return promise.then(function (result) {
                var response = result.data;
                var total = response.otherData[0];
                params.total(total);
                $scope.tableData = response.data;
                return response.data;
            });

        }
    });

    $scope.toAddTurnover = function () {
        $scope.editTurnover = false;
        $scope.formData = {};
        setFormData();
        angular.element("#addTurnoverModal2").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    //根据选择的员工设置岗位 职务 部门
    function setFormData () {
        $scope.formData.staffId = $scope.selectStaff.staffId;
        $scope.formData.name = $scope.selectStaff.name;
        $scope.formData.oldDepart = $scope.selectStaff.departName;
        $scope.formData.newDepart = $scope.selectStaff.departName;
        $scope.formData.oldPosition = $scope.selectStaff.position;
        $scope.formData.newPosition = $scope.selectStaff.position;
        $scope.formData.oldDuty = $scope.selectStaff.duty;
        $scope.formData.newDuty = $scope.selectStaff.duty;
    }

    //获取部门列表
    $scope.getDepartList = function () {
        //获取所有部门
        $http.get("rest/depart/allList", null).success(function (data) {
            if (data !== null && data !== undefined) {
                $scope.departList = data;
                console.log("depart/lis", data);
            }
        }).error(function (data) {
            angular.element("#serverErrorModal").modal({show: true});
        });
    };
    $scope.getDepartList();

    //异常类型改变触发
    $scope.typeChange = function (val) {
        $scope.showAction = val === "转正";
    };

    //新增和修改异动事项
    $scope.addTurnover = function () {
        console.log($scope.formData);
        if (!$scope.formData.effectTime) {
            showOkModal("提示", "生效日期不能为空!");
            return;
        }
        //修改
        if ($scope.formData.id) {
            $http.put("rest/turnover/update", $scope.formData).success(function (data) {
                if (data != null && data != undefined) {
                    showOkModal('提示', data.message);
                    if (data.resultCode == "RESULT_SUCCESS") {
                        self.tableParams.reload();
                        angular.element("#addTurnoverModal").modal('hide');
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });

        } else {//新增
            $http.post("rest/turnover/add", $scope.formData).success(function (data) {
                if (data !== null && data !== undefined) {
                    showOkModal('提示', data.message);
                    if (data.resultCode === "RESULT_SUCCESS") {
                        self.tableParams.reload();
                        angular.element("#addTurnoverModal2").modal('hide');
                        $scope.selectStaff.departName=$scope.formData.newDepart;
                        $scope.selectStaff.duty=$scope.formData.newDuty;
                        $scope.selectStaff.position=$scope.formData.newPosition;
                        if($scope.formData.type=='离职'){
                            $scope.selectStaff.onJob='离职';
                        }else if($scope.formData.type=='入职'){
                            $scope.selectStaff.entryDate=$scope.formData.effectTime;
                            $scope.selectStaff.onJob='在职';
                        }else if($scope.formData.type=='转正' && $scope.formData.action=='正常转正'){
                            $scope.selectStaff.onJob='在职';
                            $scope.selectStaff.isFormal=1;
                        }else{
                            $scope.selectStaff.onJob='在职';
                        }
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        }

    };

    //删除异动信息
    $scope.toDelTurnover = function (index) {
        showConfirmModal("确定要删除吗？", function () {
            var id = $scope.tableData[index].id;
            $http.delete("rest/turnover/" + id + "/del").success(function (data) {
                if (data !== null && data !== undefined) {
                    if (data.resultCode === "RESULT_SUCCESS") {
                        self.tableParams.reload();
                    } else {
                        showOkModal('提示', data.message);
                    }
                }
            }).error(function (data) {
                angular.element("#serverErrorModal").modal({show: true});
            });
        });
    };

    //准备修改异动信息
    $scope.toEditTurnover = function (index) {
        $scope.editTurnover = true;
        console.log($scope.tableData);
        var row = $scope.tableData[index];
        $scope.formData.id = row.id;
        $scope.formData.staffId = row.staffId;
        $scope.formData.staffName = row.staffName;
        $scope.formData.oldDepart = row.oldDepart;
        $scope.formData.newDepart = row.newDepart;
        $scope.formData.oldPosition = row.oldPosition;
        $scope.formData.newPosition = row.newPosition;
        $scope.formData.oldDuty = row.oldDuty;
        $scope.formData.newDuty = row.newDuty;
        $scope.formData.type = row.type;
        $scope.formData.effectTime = row.effectTime;

        angular.element("#addTurnoverModal2").modal({show: true, keyboard: false, backdrop: 'static'});
    };

    /************************************异动信息管理   结束**************************************/

    //将所选择的时间格式化
    $scope.onTimeSet = function (newDate, oldDate) {
        $scope.formatterTime();
    };

    $scope.formatterTime = function () {
        if ($scope.formData.endDate) {
            $scope.formData.endDate = DateToString2(new Date($scope.formData.endDate));
        }
        if ($scope.formData.beginDate) {
            $scope.formData.beginDate = DateToString2(new Date($scope.formData.beginDate));
        }
        if ($scope.formData.borrowDate) {
            $scope.formData.borrowDate = DateToString2(new Date($scope.formData.borrowDate));
        }
        if ($scope.formData.backDate) {
            $scope.formData.backDate = DateToString2(new Date($scope.formData.backDate));
        }
        if ($scope.formData.createDate) {
            $scope.formData.createDate = DateToString2(new Date($scope.formData.createDate));
        }
        if ($scope.formData.effectTime) {
            $scope.formData.effectTime = DateToString2(new Date($scope.formData.effectTime));
        }
        if ($scope.selectStaff.graduateDate) {
            $scope.selectStaff.graduateDate = DateToString2(new Date($scope.selectStaff.graduateDate));
        }
        if ($scope.selectStaff.entryDate) {
            $scope.selectStaff.entryDate = DateToString2(new Date($scope.selectStaff.entryDate));
        }
        if ($scope.selectStaff.formalDate) {
            $scope.selectStaff.formalDate = DateToString2(new Date($scope.selectStaff.formalDate));
        }
    };
}]);
