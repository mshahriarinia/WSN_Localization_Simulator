%A = [-5 3 2; -10 10 11; -15 6 7; -20 15 14]

% X = [0, 1 ];
% Y = [0, 1];
% Z = [0, 0
%      1, 3];
%  surf(X,Y,Z);
% 
% 
% 
% 
% A = csvread('C:\z Personal\ShahriariNia\Workbench\Workspaces\WorkspaceSim\simulator\outputs\diag\diag 25.txt');
% X = A(:,1);
% Y = A(:,2);
% % 
% 
% Z = zeros(size(X));
% for i = 1:size(X)
%     
%     Z(i,i) = A(i,3);
%    
%     
% end
% 
% %figure, mesh(X,Y,Z);
% figure, surf(X,Y,Z);
% xlabel('x-axis'),ylabel('y-axis'),zlabel('error of node at (x, y)');


x = -2:.2:2;
y = -2:.2:2;

[X,Y] = meshgrid(x, y);    

Z = X .* exp(-X.^2 - Y.^2); 
surf(X,Y,Z)