@rem ***************************************************************************
@rem Copyright 2015 xWic group (http://www.xwic.de)
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License").
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem  		http://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software 
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and 
@rem limitations under the License.
@rem ***************************************************************************
@echo off
: Sets the standard classpath

echo setting xWic Sandbox Platform classpath
for %%i in (..\lib\*.jar) do call :setClassPath %%i
goto :EOF

:setClassPath
set SANDBOXCP=%1;%SANDBOXCP%
goto :EOF
