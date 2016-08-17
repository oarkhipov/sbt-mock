/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Created by sbt-bochev-as on 04.07.2016.
 */

"$(\"a:contains('Copyright')\").click(function () { BootstrapDialog.show({type: BootstrapDialog.TYPE_SUCCESS, title: 'Copyright', message: \"<div style='text-align: center'><b>АС 'Эмулятор'</b><br/> Разработан Отделом Автоматизации и Юнит тестирования,<br/> Центр Компетенции №7,<br/>Сбербанк Технологии.<br/><br/> 2014-2016©</div>\" }); }); valid = true;"

"var closeButton = Math.round(Math.random()*6 + 1);    console.log(closeButton);    if (valid != true) {        BootstrapDialog.show({            title: 'Copyright',            closable: false,            type: BootstrapDialog.TYPE_DANGER,            message: \"<div style='text-align: center'>Код авторских прав был удалён!<br/>Обратитесь к администратору.</div>\",            buttons: [{                label: 'Close 1',                action: function (dialogRef) {                    if(closeButton == 1) {                        dialogRef.close();                    }                }            }, {                label: 'Close 2',                action: function (dialogRef) {                    if(closeButton == 2) {                        dialogRef.close();                    }                }            }, {                label: 'Close 3',                action: function (dialogRef) {                    if(closeButton == 3) {                        dialogRef.close();                    }                }            }, {                label: 'Close 4',                action: function (dialogRef) {                    if(closeButton == 4) {                        dialogRef.close();                    }                }            }, {                label: 'Close 5',                action: function (dialogRef) {                    if(closeButton == 5) {                        dialogRef.close();                    }                }            }, {                label: 'Close 6',                action: function (dialogRef) {                    if(closeButton == 6) {                        dialogRef.close();                    }                }            }, {                label: 'Close 7',                action: function (dialogRef) {                    if(closeButton == 7) {                        dialogRef.close();                    }                }            }]        });    }"
